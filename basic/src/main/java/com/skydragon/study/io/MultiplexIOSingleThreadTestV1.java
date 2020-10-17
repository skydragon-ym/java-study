package com.skydragon.study.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

//第三阶段：多路复用器
public class MultiplexIOSingleThreadTestV1 {
    //private ByteBuffer sendbuffer = ByteBuffer.allocate(4096);
    //private ByteBuffer recvbuffer = ByteBuffer.allocate(4096);
    private Selector selector;

    public static void main(String[] args) throws IOException {
        MultiplexIOSingleThreadTestV1 server = new MultiplexIOSingleThreadTestV1();
        server.start();
    }

    public void start() throws IOException {
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
        server.register(selector,SelectionKey.OP_ACCEPT);

        System.out.println("Server started...");

        while (true){
            //Set<SelectionKey> keys = selector.keys();

            //This method performs a blocking selection operation. It returns only after at least one channel is selected,
            // this selector's wakeup method is invoked, or the current thread is interrupted, whichever comes first.
            while (selector.select()>0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    iterator.remove(); //不移除会重复循环处理
                    if(key.isAcceptable()){
                        acceptHandler(key);
                    }else if(key.isReadable()){
                        readHandler(key);
                    }else if(key.isWritable()){
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
        System.out.println("read handler.....");
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        try {
            while (true) {
                read = client.read(buffer);
                if (read > 0) {
                    client.register(key.selector(),SelectionKey.OP_WRITE,buffer);
                    //关心  OP_WRITE 其实就是关系send-queue是不是有空间
                } else if (read == 0) {
                    break;
                } else {
                    client.close();
                    break;
                }
            }
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            System.out.println("server read data: " + new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHandler(SelectionKey key) throws ClosedChannelException {
        System.out.println("write handler...");
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
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buffer.clear();
        //key.cancel(); //调用了cancel后，读写事件就都监听不到了?
        //取消关注可写事件，只关注只读事件，这是一个系统调用，会产出用户态内核态的切换
        client.register(selector, SelectionKey.OP_READ, buffer);

        /*
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }

}
