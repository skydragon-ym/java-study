package com.skydragon.study.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/*
单机连接数的问题
 */
public class C10KClient {
    public static void main() throws IOException {
        LinkedList<SocketChannel> clients = new LinkedList<>();
        InetSocketAddress serverAddr = new InetSocketAddress("192.168.75.130", 9090);

        for(int i=10000;i<65000;i++){
            SocketChannel client1 = SocketChannel.open();
            SocketChannel client2 = SocketChannel.open();

            client1.bind(new InetSocketAddress("192.168.75.129",i));
            client1.connect(serverAddr);
            clients.add(client1);

            client2.bind(new InetSocketAddress("192.168.75.1",i));
            client2.connect(serverAddr);
            clients.add(client2);
        }

    }
}
