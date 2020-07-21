package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class WaitNotify {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        List list = Collections.synchronizedList(new ArrayList());
        //t1
        Thread t1 = new Thread(()->{
            synchronized (lock) {
                for (int i = 1; i <= 10; i++) {
                    list.add(i);
                    System.out.println(list.size());
                    if (i == 5) {
                        LockSupport.park();
                    }
                }
            }
        });

        //t2
        Thread t2 = new Thread(()->{
            synchronized (lock){
                System.out.println("T2 Start");

                System.out.println("T2 End");
            }
        });

        t2.start();
        //Thread.sleep(1000);
        t1.start();
    }
}
