package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class WaitNotify {
    static Thread t1, t2;
    public static void main(String[] args) throws InterruptedException {
        List list = Collections.synchronizedList(new ArrayList());

        Object lock = new Object();

        t1 = new Thread(()->{
            synchronized (lock) {
                for (int i = 1; i <= 10; i++) {
                    list.add(i);
                    System.out.println(list.size());
                    if (i == 5) {

                    }
                }
            }
        });

        t2 = new Thread(()->{
            synchronized (lock){
                System.out.println("T2 Start");

                System.out.println("T2 End");
            }
        });

        t1.start();
        Thread.sleep(0);
        t2.start();
        //Thread.sleep(1000);
    }
}
