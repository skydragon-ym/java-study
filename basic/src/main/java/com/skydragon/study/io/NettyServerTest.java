package com.skydragon.study.io;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.dubbo.qos.server.Server;

import java.net.InetSocketAddress;

public class NettyServerTest {
    public static void main(String[] args){
        NettyServer myServer = new NettyServer();
        try {
            myServer.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class NettyServer{
    public NettyServer(){
    }

    public void start() throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(1);
        Bootstrap bs = new Bootstrap();

        ChannelFuture server = bs.group(group)
                .channel(ServerSocketChannel.class)
                .bind(new InetSocketAddress("localhost", 9090));

        server.sync().channel().closeFuture().sync();
    }

}

