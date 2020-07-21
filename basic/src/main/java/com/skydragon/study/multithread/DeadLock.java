package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class DeadLock {
    public static void main(String[] args) throws InterruptedException {
        List list = Collections.synchronizedList(new ArrayList());
        Object lock = new Object();

        //t2
        //线程2进入同步块，但又调用park()方法暂停了，永远不释放锁，线程1永远拿不到锁
        Thread t2 = new Thread(()-> {
            synchronized (lock) {
                System.out.println("T2 Start");
                LockSupport.park();
                //do something here...
                System.out.println("T2 End");
            }
        });

        //t1
        Thread t1 = new Thread(()->{
            synchronized (lock) {
                for (int i = 1; i <= 10; i++) {
                    list.add(i);
                    System.out.println(list.size());
                    if (i == 5) {
                        LockSupport.unpark(t2);
                    }
                }
            }
        });

        t2.start();
        //Thread.sleep(1000);
        t1.start();
    }
}
