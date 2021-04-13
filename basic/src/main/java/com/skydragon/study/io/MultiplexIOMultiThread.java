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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiplexIOMultiThread {
    private ServerSocketChannel server = null;
    private Selector selector1 = null;
    private Selector selector2 = null;
    private Selector selector3 = null;
    int port = 9090;

    //Executor tp = Executors.newFixedThreadPool()

    public void initServer() throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));
        selector1 = Selector.open();
        selector2 = Selector.open();
        selector3 = Selector.open();
        server.register(selector1, SelectionKey.OP_ACCEPT);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MultiplexIOMultiThread myServer = new MultiplexIOMultiThread();
        myServer.initServer();

        NioEventLoop eventLoop_boss = new NioEventLoop(myServer.selector1, 2);
        NioEventLoop eventLoop_worker1 = new NioEventLoop(myServer.selector2);
        NioEventLoop eventLoop_worker2 = new NioEventLoop(myServer.selector3);

        eventLoop_boss.start();
        //确保boss线程完全启动
        Thread.sleep(1000);

        //启动worker线程
        eventLoop_worker1.start();
        eventLoop_worker2.start();

        System.out.println("服务器启动了");
        System.in.read();

    }
}

class NioEventLoop extends Thread{
    Selector selector = null;
    static int selectorsNum = 0;
    int id = 0;

    volatile static BlockingQueue<SocketChannel>[] queue;
    static AtomicInteger idx = new AtomicInteger();

    NioEventLoop(Selector selector, int n){
        this.selector = selector;
        selectorsNum = n;

        //为2个worker selector线程准备阻塞队列
        queue = new LinkedBlockingQueue[selectorsNum];
        for(int i=0; i<n; i++){
            queue[i] = new LinkedBlockingQueue<>();
        }
    }

    NioEventLoop(Selector selector){
        this.selector = selector;
        id = idx.getAndIncrement() % selectorsNum;
        System.out.println("worker: " + id + " 启动");
    }

    @Override
    public void run(){
        try{
            while (true){
                while(selector.select(10)>0){
                    //这里是线性同步处理
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while(iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        }
                    }
                }

                if( ! queue[id].isEmpty()) {
                    ByteBuffer buffer = ByteBuffer.allocate(8192);
                    SocketChannel client = queue[id].take();
                    client.register(selector, SelectionKey.OP_READ, buffer);
                    System.out.println("-------------------------------------------");
                    System.out.println("新客户端：" + client.socket().getPort()+"分配到："+ (id));
                    System.out.println("-------------------------------------------");
                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void acceptHandler(SelectionKey key) throws IOException {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        SocketChannel client =  ssc.accept();
        client.configureBlocking(false);

        id = idx.getAndIncrement() % selectorsNum;
        queue[id].add(client);
    }

    public void readHandler(SelectionKey key){
        SocketChannel client = (SocketChannel) key.channel();

    }

}
