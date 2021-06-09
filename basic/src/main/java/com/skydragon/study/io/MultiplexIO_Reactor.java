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
单线程模型， 这版代码是根据 Doug Lea 的 PPT 中的线程模型编写的， 也符合 Netty 的编程模型
 */
public class MultiplexIO_Reactor implements Runnable{

    final Selector selector;
    final ServerSocketChannel serverSocket;

    public MultiplexIO_Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(9090));
        serverSocket.configureBlocking(false);

        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
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
                SocketChannel c = serverSocket.accept();
                if (c != null){
                    new Handler(selector, c);
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
            //wakeup:
            selector.wakeup();
        }

        @Override
        public void run() {

        }
    }
}


