package com.skydragon.study.multithread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExceptionHandling implements Runnable{
    @Override
    public void run() {
        try{
            int i = 1/0;
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            ExecutorService exec = Executors.newCachedThreadPool();
            exec.execute(new ExceptionHandling());
        } catch (Exception e) {
            System.out.println("能不能捕获到异常？");
        }

    }
}
