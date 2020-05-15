package com.skydragon.study.multithread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierTest {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cb = new CyclicBarrier(5, ()->{
            System.out.println("all done");
        });

        for(int i=1; i<=5; i++){
            new Thread(()-> {
                try {
                    cb.await();
                    System.out.println("Thread Id:" + Thread.currentThread().getName() + "doing something");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.out.println("main thread");
    }

}
