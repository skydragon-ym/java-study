package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Arrays;

public class TwoThreadPrint2 {
    static Thread t1, t2;
    public static void main(String[] args){
        ArrayList a1 = new ArrayList(Arrays.asList(1,2,3,4,5,6));
        ArrayList a2 = new ArrayList(Arrays.asList("A","B","C","D","E","F"));

        Object lock = new Object();

        t1 = new Thread(()->{
            synchronized (lock){
                for(int i=0;i<6;i++){
                    System.out.println(a1.get(i));
                    try {
                        lock.notify();
                        lock.wait(); //release lock
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });

        t2 = new Thread(()->{
            synchronized (lock){
                for(int i=0;i<6;i++){
                    System.out.println(a2.get(i));
                    try {
                        lock.notify();
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });

        t1.start();
        t2.start();
    }
}
