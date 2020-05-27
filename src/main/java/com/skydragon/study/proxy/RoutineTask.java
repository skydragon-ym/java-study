package com.skydragon.study.proxy;

public class RoutineTask implements Task{
    @Override
    public void doWork() {
        System.out.println("Do routine task");
    }
}
