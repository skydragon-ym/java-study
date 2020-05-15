package com.skydragon.study.collection.queue;

import com.sun.corba.se.impl.orbutil.concurrent.Sync;

import java.util.PriorityQueue;
import java.util.concurrent.*;

public class LinkedBlockingQueue {
    public static void main(String[] args) {

        BlockingQueue queue = new LinkedBlockingDeque();

        //producer
        for(int i=0; i<2; i++){
            new Thread(()->{
                for(int j=0;j<20;j++){
                    try {
                        queue.put(j);
                        System.out.println("Producer " + Thread.currentThread().getName() + " put " + j);
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }).start();
        }

        //consumer
        for(int i=0; i<9;i++){
            new Thread(()->{
                for(int j=0; j<15;j++){
                    try {
                        String s = queue.take().toString();
                        TimeUnit.MILLISECONDS.sleep(50);
                        System.out.println("Consumer " + Thread.currentThread().getName() + " take " + s);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
