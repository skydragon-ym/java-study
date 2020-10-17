package com.skydragon.study.io;

public class UnchecckedExceptionhandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("捕获到异常：" + e);
    }
}
