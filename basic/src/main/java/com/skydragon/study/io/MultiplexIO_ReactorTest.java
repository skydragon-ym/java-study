package com.skydragon.study.io;

import java.io.IOException;

public class MultiplexIO_ReactorTest {
    public static void main(String[] args) throws IOException {
        MultiplexIO_Reactor_SingleThread reactor = new MultiplexIO_Reactor_SingleThread(9090);

        //启动Reactor
        new Thread(reactor).start();
    }
}
