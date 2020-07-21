package com.skydragon.study.multithread;

/**
 * Multi Thread via lambda
 *File > Settings > Build, Execution, Deployment > Compiler > Java Compiler to 1.8
 * Project Structure > Project Setting > Project > ProjectSdk to 1.8
 */
public class MultiThreadJoinLambda {
    public static void main(String[] args) throws InterruptedException {
        Thread t2 = new Thread(() -> {
            for(int i=101; i <=200; i++){
                System.out.println(i);
            }
        });

        Thread t1 = new Thread(() -> {
            t2.start();
            try {
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int i=1; i <=100; i++){
                System.out.println(i);
            }
        });

        t1.start();
    }
}
