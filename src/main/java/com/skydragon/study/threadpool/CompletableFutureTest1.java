package com.skydragon.study.threadpool;

import java.util.concurrent.*;

public class CompletableFutureTest1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();

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
                    future.complete("task complete");
                    Thread.sleep(5000);
                    System.out.println("task done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        ExecutorService es = Executors.newFixedThreadPool(2);
        es.execute(task);

        System.out.println(future.get());
        System.out.println("main done");
        es.shutdown();
    }
}
