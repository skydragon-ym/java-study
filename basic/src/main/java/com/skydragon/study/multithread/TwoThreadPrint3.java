package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TwoThreadPrint3 {
    static Thread t1, t2;
    public static void main(String[] args){
        ArrayList a1 = new ArrayList(Arrays.asList(1,2,3,4,5,6));
        ArrayList a2 = new ArrayList(Arrays.asList("A","B","C","D","E","F"));

        //CAS lock
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        t1 = new Thread(()->{
            for(int i=0;i<6;i++){
                lock.lock();
                System.out.println(a1.get(i));
                condition.signal();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });

        t2 = new Thread(()->{
            for(int i=0;i<6;i++){
                lock.lock();
                System.out.println(a2.get(i));
                condition.signal();
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        t2.start();
    }
}
