package com.skydragon.study.multithread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
    1.生产者消费者线程争抢锁，没有抢到锁的线程会被Block
    2.如果生产者线程拿到锁，判断集合是不是已经满了，如果满了会释放锁并唤醒消费者线程队列
      如果消费者线程拿到锁，判断集合是不是已经空了，如果空了会释放锁并唤醒生产者线程队列
 */
public class TwoThreadPrint3 {
    static Thread t1, t2;
    public static void main(String[] args){
        ArrayList a1 = new ArrayList(Arrays.asList(1,2,3,4,5,6));
        ArrayList a2 = new ArrayList(Arrays.asList("A","B","C","D","E","F"));

        //CAS lock
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        t1 = new Thread(()->{
            for(int i=0;i<6;i++){
                lock.lock();
                System.out.println(a1.get(i));
                condition.signal();
                try {
                    condition.await(); //release lock
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }

            }
        });

        t2 = new Thread(()->{
            for(int i=0;i<6;i++){
                lock.lock();
                System.out.println(a2.get(i));
                condition.signal();
                try {
                    condition.await(); //release lock
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    lock.unlock();
                }
            }
        });

        t1.start();
        t2.start();
    }
}
