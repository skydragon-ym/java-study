package com.skydragon.study.multithread;

/*
这个程序模拟了当setBalance执行过程当中，如果getBalance方法不加锁，主线程读取balance时读到到的是100。
如果给getBalance加锁，主线程中调用getBalance方法将会被阻塞，直到线程1的setBalance方法执行完毕释放锁。
还是要看业务逻辑是不是允许加锁。能不加就不加锁，加了锁要慢100倍。
 */

public class DirtyRead {
    private volatile double balance = 100;

    public synchronized double getBalance() {
        return balance;
    }

    public synchronized void setBalance(long balance) throws InterruptedException {
        Thread.sleep(5);
        this.balance = balance;
    }

    public static void main(String[] args) throws InterruptedException {
        DirtyRead dr = new DirtyRead();
        new Thread(()->{
            try {
                dr.setBalance(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        Thread.sleep(2);
        System.out.println(dr.getBalance());
    }
}
