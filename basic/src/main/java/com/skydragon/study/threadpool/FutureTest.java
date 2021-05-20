package com.skydragon.study.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<?> future = executor.submit(()->{
            System.out.println("async task start...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        });

        System.out.println("main thread do other things");
        Object result = future.get();
        System.out.println(result);
    }
}
