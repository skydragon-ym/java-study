package com.skydragon.study.threadpool;

import java.util.concurrent.*;

/*
FutureTask
 */
public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        FutureTask<String> task = new FutureTask<String>(()->{
            System.out.println("async task start...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("async task end...");
        }, "result 1");

        Future future = executor.submit(task, "result 2");

        System.out.println("main thread do other things");

        System.out.println(task.get());
        System.out.println(future.get());
    }
}
