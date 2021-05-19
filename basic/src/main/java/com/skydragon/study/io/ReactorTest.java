package com.skydragon.study.io;

import java.io.IOException;

public class ReactorTest {
    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(9090);

        //启动Reactor
        new Thread(reactor).start();
    }
}
