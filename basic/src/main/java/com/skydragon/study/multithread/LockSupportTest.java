package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

/*
t1线程想在t2线程写入第五个元素的时候得到通知
 */
public class LockSupportTest {
    static Thread t1 = null;
    static Thread t2 = null;
    public static void main(String[] args) throws InterruptedException {
        List list = Collections.synchronizedList(new ArrayList());
        Object lock = new Object();
        //t2
        t2 = new Thread(()-> {
            LockSupport.park();
            //do something here...
            System.out.println("T2 End");
            LockSupport.unpark(t1);
        });

        //t1
        t1 = new Thread(()->{
            for (int i = 1; i <= 10; i++) {
                list.add(i);
                System.out.println(list.size());
                if (i == 5) {
                    LockSupport.unpark(t2);
                    LockSupport.park();
                }
            }
        });

        t2.start();
        t1.start();
    }
}
