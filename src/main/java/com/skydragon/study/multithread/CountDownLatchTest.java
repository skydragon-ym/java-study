package com.skydragon.study.multithread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*
使用场景：等待多个线程都执行完之后，再开始主线程后面的逻辑
 */
public class CountDownLatchTest implements Runnable{
    static CountDownLatch cdl = new CountDownLatch(10);
    @Override
    public void run() {
        System.out.println(("Thread Id: " + Thread.currentThread().getId()));
        cdl.countDown();
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchTest task = new CountDownLatchTest();
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for(int i=1; i<=10; i++){
            exec.submit(task);
        }
        cdl.await();
        System.out.println("main thread");
        exec.shutdown();
    }

}
