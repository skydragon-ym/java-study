package com.skydragon.study.threadpool;

import java.util.concurrent.*;

/*
FutureTask: 实现 Runnable，Future 接口
 */
public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        FutureTask<Integer> task = new FutureTask<>(()->{
            System.out.println("async task start...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("async task end...");
            return 100;
        });

        Future future = executor.submit(task, "result 2");

        System.out.println("main thread do other things");

        System.out.println(task.get());
        System.out.println(future.get());
    }
}
