package com.skydragon.study.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/*
多路复用器多线程版V2，Reactor模型，线程模型是重点，这里使用的是 主-从 reactor 模型
参见架构3期，189课程，这版代码是重点，仔细读
 */
public class MultiplexIO_MultiThreadTestV2 {

    public static void main(String[] args) throws IOException {
        EventLoopGroup bossGroup = new EventLoopGroup(1);
        EventLoopGroup workerGroup = new EventLoopGroup(2);

        ServerBootStrap server = new ServerBootStrap(bossGroup, workerGroup);

        try {
            server.bind(9090);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //启动 IO 线程
        server.start();
        System.out.println("服务器启动了");
    }
}

class ServerBootStrap{
    EventLoopGroup bossGroup;
    EventLoopGroup workerGroup;

    public ServerBootStrap(EventLoopGroup group) throws IOException {
        bossGroup = workerGroup = group;
    }

    public ServerBootStrap(EventLoopGroup bossGroup, EventLoopGroup workerGroup) throws IOException {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
    }

    public void bind(int port) throws IOException, InterruptedException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(port));

        /*注册server socket
        直接注册channel到EventGroup中的selector可能会有问题。
        如果此时selector所在的线程由于select()方法没有就绪的fd而阻塞，
        主线程再向这个selector注册fd的话，主线程也会被阻塞*/
        //ssc.register(bossGroup.getEventLoop().selector, SelectionKey.OP_ACCEPT, workerGroup);

        //使用事件驱动方式注册ServerSocket
        //
        NioEventLoop eventLoop = bossGroup.nextEventLoop();
        eventLoop.execute(()->{
            try {
                ssc.register(eventLoop.selector, SelectionKey.OP_ACCEPT, workerGroup);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });

    }

    public void start(){
        bossGroup.start();
        workerGroup.start();
    }
}

class EventLoopGroup {
    ExecutorService executorService;

    AtomicInteger cid = new AtomicInteger(0);
    NioEventLoop[] eventLoops;

    public EventLoopGroup(int nThreads) throws IOException {
        eventLoops = new NioEventLoop[nThreads];
        for(int i =0; i < nThreads; i++){
            eventLoops[i] = new NioEventLoop();
        }
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    //从 Group 中选择一个 EventLoop
    public NioEventLoop nextEventLoop(){
        return eventLoops[cid.getAndIncrement() % eventLoops.length];
    }

    public void start(){
        for(NioEventLoop eventGroup : eventLoops){
            executorService.submit(eventGroup);
        }
    }
}

class NioEventLoop implements Runnable{
    Selector selector;
    BlockingQueue events = new LinkedBlockingQueue();

    public NioEventLoop() throws IOException {
        selector = Selector.open();
    }

    //event loop
    @Override
    public void run() {
        try {
            while (true) {
                /*重点：selector.select()如果当前监控的fds没有就绪事件，则一直阻塞
                这时如果有新的client fd需要注册到这个selector的话，需要调用selector.wakeup方法唤醒线程
                */
                while (selector.select() > 0) {
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            acceptHandler(key);
                        } else if (key.isReadable()) {
                            readHandler(key);
                        } else if (key.isWritable()) {

                        }
                    }
                }

                /*如果代码执行到这里，说明selector.select()方法返回0，代表没有就绪的fd，
                那么一定是主线程推送了task到队列中，并调用了selector.wakeup方法
                */
                runTask();
                
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void execute(Runnable task) throws InterruptedException {
        this.events.put(task);
        this.selector.wakeup();
    }

    public void runTask() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Runnable task = (Runnable) events.poll(10, TimeUnit.MILLISECONDS);
            if (task != null) {
                events.remove(task);
                task.run();
            }
        }
    }

    public void acceptHandler(SelectionKey key) throws IOException, InterruptedException {
        ServerSocketChannel ssc =  (ServerSocketChannel) key.channel();
        SocketChannel client = ssc.accept();
        client.configureBlocking(false);

        //取得 WorkerGroup 用于将 client 分配到一个 worker 上
        EventLoopGroup workerGroup = (EventLoopGroup)key.attachment();

        //注册client socket
        //client.register(workerGroup.getEventLoop().selector, SelectionKey.OP_READ, null);
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        NioEventLoop eventLoop = workerGroup.nextEventLoop();
        eventLoop.execute(()->{
            try {
                client.register(eventLoop.selector, SelectionKey.OP_READ, buffer);
                System.out.println("-------------------------------------------");
                System.out.println("新客户端：" + client.getRemoteAddress());
                System.out.println("-------------------------------------------");

            } catch (ClosedChannelException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
                System.out.println("客户端：" + client.getRemoteAddress() + " 断开连接");
                key.cancel();
                client.close();
                break;
            }
        }
    }

}
