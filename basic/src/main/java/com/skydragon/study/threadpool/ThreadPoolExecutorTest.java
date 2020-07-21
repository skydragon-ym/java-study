package com.skydragon.study.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorTest {
    public static void main(String[] args){
        ThreadPoolExecutor tpExecutor = new ThreadPoolExecutor(2,4,0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
