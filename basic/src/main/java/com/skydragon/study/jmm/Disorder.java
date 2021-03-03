package com.skydragon.study.jmm;

public class Disorder {
    static int a,b=0;
    static int x,y=0;
    public static void main(String[] args) throws InterruptedException {
        long i=0;
        while(true) {
            i++;

            Thread t1 = new Thread(() -> {
                a = 1;
                x = b;
            });

            Thread t2 = new Thread(() -> {
                b = 1;
                y = a;
            });

            t1.start();
            t2.start();
            t1.join();
            t2.join();
            if(x==0 && y==0){
                System.out.println("第\" + i + \"次 (\" + x + \",\" + y + \"）");
            }

        }

    }
}
