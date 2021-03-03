package com.skydragon.study.multithread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalTest {
    public static ExecutorService threadPool = Executors.newFixedThreadPool(3);

    public static ThreadLocal<String> tlData1 = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "Test1";
        }
    };

    public static ThreadLocal<String> tlData2 = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return "Test2";
        }
    };

    public static void main(String[] args){
        //Thread-1
        threadPool.execute(()->{
            //下面2个语句会在当前线程Thread-1中的ThreadLocalMap中插入2个Entry, <tlData1,value1>和<tlData2,value2>
            tlData1.set("Thread-1-Value-1");
            tlData2.set("Thread-1-Value-2");
            System.out.println(Thread.currentThread().getName() + "-->" +tlData1.get());
            System.out.println(Thread.currentThread().getName() + "-->" +tlData2.get());
        });

        //Thread-2
        threadPool.execute(()->{
            //下面2个语句会在当前线程Thread-2中的ThreadLocalMap中插入2个Entry, <tlData1,value3>和<tlData2,value4>
            tlData1.set("Thread-2-Value-3");
            tlData2.set("Thread-2-Value-4");
            System.out.println(Thread.currentThread().getName() + "-->" +tlData1.get());
            System.out.println(Thread.currentThread().getName() + "-->" +tlData2.get());
        });

        tlData1.remove();
        tlData2.remove();
    };

}
