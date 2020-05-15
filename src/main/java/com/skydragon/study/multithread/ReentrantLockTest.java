package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/*
生产者消费者问题
*/
public class ReentrantLockTest {

    static class SyncContainer<T>{
        int MAX_SIZE = 10;
        List<T> list = new ArrayList<T>();
        ReentrantLock lock = new ReentrantLock();
        Condition consumer = lock.newCondition();
        Condition producer = lock.newCondition();

        public void add(T value) throws InterruptedException {
            try {
                lock.lock();
                while (list.size() == MAX_SIZE) {
                    producer.await();
                }
                list.add(value);
                System.out.println("Add element: " + value);
                consumer.signalAll();
            }
            finally {
                lock.unlock();
            }
        }

        public void remove(T value) throws InterruptedException {
            lock.lock();
            try{
                while (list.size() == 0) {
                    consumer.await();
                }
                list.remove(value);
                System.out.println("Remove element: " + value);
                producer.signalAll();
            }
            finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args){
        SyncContainer<Integer> myContainer = new SyncContainer<Integer>();

        //create producer thread
        for(int i=1; i<=2; i++) {
            new Thread(() -> {
                for(int j=1; j<=20; j++){
                    try {
                        myContainer.add(j);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        //create consumer thread
        for(int i=1; i<=8; i++){
            new Thread(()->{
                for(int j=1; j<=15; j++){
                    try {
                        myContainer.remove(j);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
