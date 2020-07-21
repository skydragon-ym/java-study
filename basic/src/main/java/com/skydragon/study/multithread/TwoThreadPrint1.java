package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.LockSupport;

/*

 */
public class TwoThreadPrint1 {
    static Thread t1, t2;
    public static void main(String[] args){
        ArrayList a1 = new ArrayList(Arrays.asList(1,2,3,4,5,6));
        ArrayList a2 = new ArrayList(Arrays.asList("A","B","C","D","E","F"));

        t1 = new Thread(()->{
            for(int i=0;i<6;i++){
                System.out.println(a1.get(i));
                LockSupport.unpark(t2);
                LockSupport.park();
            }
            LockSupport.unpark(t2);
        });

        t2 = new Thread(()->{
            for(int i=0;i<6;i++){
                LockSupport.park();
                System.out.println(a2.get(i));
                LockSupport.unpark(t1);
            }
        });

        t1.start();
        t2.start();
    }
}
