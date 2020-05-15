package com.skydragon.study.multithread;

import java.util.concurrent.Semaphore;

/*
使用场景：限流
 */
public class SemaphoreTest {
    static Semaphore s = new Semaphore(2);
    public static void main(String[] args){
        for(int i=1; i<=10; i++){
            new Thread(()->{
                try {
                    s.acquire();
                    System.out.println("Thread Name: " + Thread.currentThread().getName() + " is doing something");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    s.release();
                }
            }).start();
        }
    }
}
