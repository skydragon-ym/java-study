package com.skydragon.study.collection.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class ConcurrentLinkedQueueTest {
    public static void main(String[] args) {
        Queue<String> tickets = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 100; i++) {
            tickets.add("票 编号" + i);
        }

        for (int i = 0; i < 10; i++)
            new Thread(() -> {
                while (true) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String s = tickets.poll();
                    if(s == null) break;
                    System.out.println(s);
                }
            }).start();
    }
}
