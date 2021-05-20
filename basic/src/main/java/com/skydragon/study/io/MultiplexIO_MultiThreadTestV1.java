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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/*
多路复用器多线程版V1，这里的多线程是指多个IO线程，将所有接入的fds分摊到多个的selector上
这一版只是过度代码，简单的演示了利用多个selector来并行处理fds
 */
public class MultiplexIO_MultiThreadTestV1 {
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

        //用于接入client
        selector1 = Selector.open();
        server.register(selector1, SelectionKey.OP_ACCEPT);

        //处理client读写事件
        selector2 = Selector.open();
        selector3 = Selector.open();

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        MultiplexIO_MultiThreadTestV1 myServer = new MultiplexIO_MultiThreadTestV1();
        myServer.initServer();

        //创建IO线程
        SelectorThread eventLoop_boss = new SelectorThread(myServer.selector1, 2);
        SelectorThread eventLoop_worker1 = new SelectorThread(myServer.selector2);
        SelectorThread eventLoop_worker2 = new SelectorThread(myServer.selector3);

        //确保boss线程完全启动
        eventLoop_boss.start();
        Thread.sleep(1000);

        //启动worker线程
        eventLoop_worker1.start();
        eventLoop_worker2.start();

        System.out.println("服务器启动了");
        System.in.read();

    }
}

/*
Selector Thread
 */
class SelectorThread extends Thread{
    Selector selector = null;
    int id = 0;

    //以下2个变量都是static的，这样可以在不同的SelectorThread实例之间共享
    static int selectorsNum = 0;
    volatile static BlockingQueue<SocketChannel>[] queues;
    static AtomicInteger idx = new AtomicInteger();

    //for boss thread
    SelectorThread(Selector selector, int n){
        this.selector = selector;
        selectorsNum = n;

        //为2个worker selector线程准备阻塞队列，存放接入的fds
        queues = new LinkedBlockingQueue[selectorsNum];
        for(int i=0; i<n; i++){
            queues[i] = new LinkedBlockingQueue<>();
        }
    }

    //for worker thread
    SelectorThread(Selector selector){
        this.selector = selector;
        id = idx.getAndIncrement() % selectorsNum;
        System.out.println("worker: " + id + " 启动");
    }

    @Override
    public void run(){
        try{
            while (true){
                //这里设置了timeout的时间，就不再需要其他线程再调用wakeup方法
                //但是这种设置timeout的方式在高并发下会严重影响系统吞吐量！
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
                        else if(key.isWritable()){

                        }
                    }
                }

                //NioEventLoop关联的client fd 队列，并注册读事件
                if( ! queues[id].isEmpty()) {
                    ByteBuffer buffer = ByteBuffer.allocate(8192);
                    //从队列中取出1个client fd，注册读事件
                    SocketChannel client = queues[id].take();
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

        //将client fd注册到worker的selector上
        id = idx.getAndIncrement() % selectorsNum;
        queues[id].add(client);
    }

    public void readHandler(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer)key.attachment();
        buffer.clear();
        int count = 0;

        while(true){
            count = client.read(buffer);
            if(count > 0){
                buffer.flip();
                while(buffer.hasRemaining()){
                    client.write(buffer);
                }
                buffer.clear();
            }
            else if(count ==0){
                break;
            }
            else if(count < 0){
                //客户端断开连接了
                System.out.println("客户端：" + client.getRemoteAddress()+"断开连接");
                key.cancel();
                client.close();
                break;
            }
        }
    }

}
