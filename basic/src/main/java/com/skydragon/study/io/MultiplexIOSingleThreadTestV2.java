package com.skydragon.study.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/*
为了避免顺序处理fd，某个fd处理比较耗时而阻塞后面fd处理的情况，引入多线程版本处理fd的读写逻辑
架构1期，187，1:50分钟左右讲到了这部分知识
 */
public class MultiplexIOSingleThreadTestV2 {
    private Selector selector;

    public static void main(String[] args) throws IOException, InterruptedException {
        MultiplexIOSingleThreadTestV2 server = new MultiplexIOSingleThreadTestV2();
        server.start();
    }

    public void start() throws IOException, InterruptedException {
        //server socket fd4
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        //listen on socket, 假设得到fd4
        server.bind(new InetSocketAddress(9090));

        //优先选择epoll模型，可以通过jvm启动参数调整
        //如果是epoll模型，则调用epoll_create, 假设得到epfd->fd3
        selector = Selector.open();

        /*
        epoll_ctl
        如果是select/poll模型，jvm开辟数组，fd放进去
        如果是epoll模型，则调用epoll_ctl(fd3,ADD,fd4,EPOLLIN)
         */
        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started...");

        while (true){
            //Set<SelectionKey> keys = selector.keys();

            //This method performs a blocking selection operation. It returns only after at least one channel is selected,
            // this selector's wakeup method is invoked, or the current thread is interrupted, whichever comes first.
            while (selector.select(50)>0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove(); //不移除会重复循环处理
                    if(key.isAcceptable()){
                        acceptHandler(key);
                    }else if(key.isReadable()){
                        key.cancel();
                        //这个方法放到了一个线程中运行，因此不会阻塞，马上返回到select()方法，此时如果另外线程中的readHandler并没有处理完fd缓冲区的数据
                        //基于多路复用器水平触发规则，这个fd还会被认为是有数据的状态，因此又会重复执行readerHandler方法
                        readHandler(key);
                    }else if(key.isWritable()){
                        key.cancel();
                        //key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
                        writeHandler(key);
                    }
                }
            }

        }
    }

    private void acceptHandler(SelectionKey key){
        try {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            client.register(selector, SelectionKey.OP_READ, buffer);
            System.out.println("-------------------------------------------");
            System.out.println("新客户端：" + client.getRemoteAddress());
            System.out.println("-------------------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readHandler(SelectionKey key) {
        Thread t = new Thread(()->{
            System.out.println("Thread:" +  Thread.currentThread().getId() + " read handler.....");
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            buffer.clear();
            int read = 0;
            try {
                while (true) {
                    read = client.read(buffer);
                    if (read > 0) {
                    } else if (read == 0) {
                        break;
                    } else {
                        client.close();
                        break;
                    }
                }
                if(buffer.position()>0){
                    buffer.flip();
                    byte[] data = new byte[buffer.limit()];
                    buffer.get(data);
                    System.out.println("server read data: " + new String(data));
                    //关心  OP_WRITE 其实就是关系send-queue是不是有空间
                    client.register(selector,SelectionKey.OP_WRITE,buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.setUncaughtExceptionHandler(new UnchecckedExceptionhandler());
        t.start();
    }

    private void writeHandler(SelectionKey key) throws ClosedChannelException {
        new Thread(()->{
            System.out.println("Thread:" + Thread.currentThread().getName() + " write handler...");
            SocketChannel client = (SocketChannel) key.channel();
            ByteBuffer buffer = (ByteBuffer) key.attachment();
            buffer.flip();
            while (buffer.hasRemaining()) {
                try {
                    client.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            buffer.clear();
            //key.cancel();
            try {
                client.register(selector, SelectionKey.OP_READ, buffer);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        }).start();

        /*
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }
}
