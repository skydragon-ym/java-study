package com.skydragon.study.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/*
单线程，单selector模型， 这版代码是根据 Doug Lea 的 PPT 中的线程模型编写的， 也符合 Netty 的编程模型
 */
public class MultiplexIO_Reactor_SingleThread implements Runnable{

    final Selector selector;
    final ServerSocketChannel serverSocket;

    public MultiplexIO_Reactor_SingleThread(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(9090));
        serverSocket.configureBlocking(false);

        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        //Acceptor用于接入client连接
        sk.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set selected = selector.selectedKeys();
                Iterator it = selected.iterator();
                while (it.hasNext())
                    dispatch((SelectionKey)(it.next()));
                selected.clear();
            }
        } catch (IOException ex) { /* ... */ }
    }

    void dispatch(SelectionKey k) {
        Runnable r = (Runnable)(k.attachment());
        if (r != null)
            r.run();
    }

    class Acceptor implements Runnable{
        @Override
        public void run() {
            try {
                SocketChannel client = serverSocket.accept();
                if (client != null){
                    //新接入的 client
                    new Handler(selector, client);
                    System.out.println("-------------------------------------------");
                    System.out.println("新客户端：" + client.getRemoteAddress());
                    System.out.println("-------------------------------------------");
                }
            }
            catch(IOException ex) { /* ... */ }

        }
    }

    class Handler implements Runnable{
        final SocketChannel clientSocket;
        final SelectionKey sk;
        final int MAXIN = 4096;
        final int MAXOUT = 4096;

        ByteBuffer input = ByteBuffer.allocate(MAXIN);
        ByteBuffer output = ByteBuffer.allocate(MAXOUT);

        static final int READING = 0, SENDING = 1;
        int state = READING;

        public Handler(Selector selector, SocketChannel socket) throws IOException {
            clientSocket = socket;
            socket.configureBlocking(false);
            sk = socket.register(selector, 0);
            sk.interestOps(SelectionKey.OP_READ);
            sk.attach(this);
            //Doug Lea: It is not always strictly required, but is good practice in case code runs in a context where it is necessary.
            selector.wakeup();
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(4096);
            try {
                if(state == READING) read();
                else if(state == SENDING) send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void read() throws IOException {
            clientSocket.read(input);
        }

        void send() throws IOException {
            clientSocket.write(output);
        }
    }
}


