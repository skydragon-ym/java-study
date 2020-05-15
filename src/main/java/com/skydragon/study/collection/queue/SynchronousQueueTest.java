package com.skydragon.study.collection.queue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/*
两个线程交替打印集合元素
*/
public class SynchronousQueueTest {
    static Thread t1,t2;
    public static void main(String[] args){
        ArrayList a1 = new ArrayList(Arrays.asList(1,2,3,4,5,6));
        ArrayList a2 = new ArrayList(Arrays.asList("A","B","C","D","E","F"));

        BlockingQueue q = new SynchronousQueue();

        t1 = new Thread(()->{
            for(int i=0;i<6;i++){
                try {
                    q.put(a1.get(i));
                    String s = (String)q.take();
                    System.out.println(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t2 = new Thread(()->{
            for(int i=0;i<6;i++){
                try {
                    Integer s = (Integer) q.take();
                    System.out.println(s);
                    q.put(a2.get(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }
}
