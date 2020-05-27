package com.skydragon.study.proxy;

public class StaticProxy implements Task {
    private static final RoutineTask myTask = new RoutineTask();
    @Override
    public void doWork() {
        System.out.println("enter static proxy...");
        myTask.doWork();
        System.out.println("exit static proxy...");
    }

    public static void main(String[] args){
        StaticProxy proxy = new StaticProxy();
        proxy.doWork();
    }

}
