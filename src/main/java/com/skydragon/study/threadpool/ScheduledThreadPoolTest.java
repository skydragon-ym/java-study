package com.skydragon.study.threadpool;

import java.io.IOException;
import java.sql.Time;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolTest {
    public static void main(String[] args) throws IOException {
        ScheduledExecutorService es = Executors.newScheduledThreadPool(5);

        for(int i=0;i<5;i++) {
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("task " + Thread.currentThread().getName() + " done");
                }
            };

            //es.schedule(task, 2, TimeUnit.SECONDS);
            es.scheduleAtFixedRate(task,0,5,TimeUnit.SECONDS);
        }

        System.in.read();
        es.shutdown();
    }
}
