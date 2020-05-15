package com.skydragon.study.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask task = new FutureTask(()->{
            System.out.println("runnable task");
        }, "done");

        new Thread(task).start();
        System.out.println(task.get());
    }
}
