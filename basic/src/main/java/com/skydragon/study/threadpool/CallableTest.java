package com.skydragon.study.threadpool;

import java.util.concurrent.*;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<String> c = new Callable(){
            @Override
            public String call() throws InterruptedException {
                TimeUnit.MILLISECONDS.sleep(2000);
                return "Hello Callable";
            }
        };

        //ExecutorService singleExecutorService = Executors.newSingleThreadExecutor();
        //ThreadPoolExecutor threadPoolExecutor2 = (ThreadPoolExecutor) singleExecutorService;

        ExecutorService es = Executors.newSingleThreadExecutor();
        Future future = es.submit(c);

        //这里创建线程对象是多余的，execute()方法内部会创建线程
        //用匿名类实现Runnable即可
        Thread t = new Thread(()->{
            System.out.println("thread run");
        });
        es.execute(t);

        System.out.println(future.get());
        es.shutdown();

        /*
        FutureTask task = new FutureTask(c);
        new Thread(task).start();
        System.out.println(task.get());
         */

    }
}
