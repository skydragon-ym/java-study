package com.skydragon.study.multithread;

/**
 * Hello world!
 *
 */
public class MultiThreadJoin
{
    private static class T1 extends Thread
    {
        @Override
        public void run() {
            T2 t2 = new T2();
            t2.start();
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for(int i=1; i <=100; i++){
                System.out.println(i);
            }
            System.out.println("---------");
        }
    }

    private static class T2 extends Thread
    {
        @Override
        public void run() {
            for(int i=101; i <=200; i++){
                System.out.println(i);
            }
            System.out.println("---------");
        }
    }

    public static void main( String[] args ) throws InterruptedException {
        T1 t1 = new T1();
        t1.start();
    }
}
