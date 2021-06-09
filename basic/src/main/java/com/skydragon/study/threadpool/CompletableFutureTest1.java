package com.skydragon.study.threadpool;

import java.util.concurrent.*;

/*
CompletableFuture 特性1：
 */
public class CompletableFutureTest1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> promise = new CompletableFuture<>();

        /*
       new Thread(()->{
            try {
                future.complete("task complete");
                Thread.sleep(5000);
                System.out.println("task done");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        */
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    promise.complete("result value");
                    System.out.println("complete task");

                    Thread.sleep(5000);
                    System.out.println("async task done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(task);

        System.out.println(promise.get());
        System.out.println("main thread do other things...");
        es.shutdown();
    }
}
