package com.skydragon.study.io;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

public class NIOSocketTest {
    //NIO
    public static void main(String[] args) {
        LinkedList<SocketChannel> clients = new LinkedList<>();

        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(9090));
            serverSocketChannel.configureBlocking(false);

            while(true){
                /*
                对于非阻塞Socket，accept不阻塞，直接返回null，这样可以增大整个系统的吞吐量
                 */
                SocketChannel clientSocketChannel = serverSocketChannel.accept();

                if(clientSocketChannel != null){
                    int port = clientSocketChannel.socket().getPort();
                    System.out.println("client...port: " + port);
                    clientSocketChannel.configureBlocking(false);
                    clients.add(clientSocketChannel);
                }

                ByteBuffer buffer = ByteBuffer.allocateDirect(4096);

                /*
                C10K问题，每循环一次，都会有1万次的channel.read()/recv()系统调用，频繁的用户态内核态的切换
                但实际情况是每一次循环可能只有少数的socket上有数据，剩下大部分的socket没有数据到达，只是返回-1
                造成了大量的无用的系统调用，且复杂度为O(n)
                 */
                for(SocketChannel c : clients){
                    int num = c.read(buffer);
                    if(num > 0){
                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);
                        System.out.println(c.socket().getPort() + " : " + new String(data));
                        buffer.clear();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
