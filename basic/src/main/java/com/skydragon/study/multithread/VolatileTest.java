package com.skydragon.study.multithread;
/*
    1.保证线程可见性
    2.禁止指令重排序(JVM指令)
 */

public class VolatileTest {
    private  boolean running = true;
    private void m(){
        while (running){
        }
        System.out.println("m end!");
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileTest vt = new VolatileTest();
        new Thread(vt::m).start();
        Thread.sleep(1000);
        vt.running = false;
    }
}
