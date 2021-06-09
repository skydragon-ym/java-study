package com.skydragon.study.threadpool;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CompletableFutureTest2 {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Double> future1 = CompletableFuture.supplyAsync(CompletableFutureTest2::callServiceA);
        //CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(()->callServiceB());

        //whenComplete 用法
        future1.whenComplete((s,throwable) ->{
            System.out.println("thread:" + Thread.currentThread().getName() + " future1 task done.");
        });

        /* 一般使用上面的写法
        future1.whenComplete(new BiConsumer<Double, Throwable>() {
            @Override
            public void accept(Double t, Throwable action) {
                System.out.println("thread:" +Thread.currentThread().getName() + " future1 task done");
            }
        });
         */

        System.out.println("main thread do other things...");

        future1.join();
        //CompletableFuture.allOf(future1,future2).join();

        //函数式的流式API调用
        //future1.thenAccept(s -> System.out.println(s)) //thenAccept(System.out::println)
                //.thenApply(o -> String.valueOf(o));//thenApply(String::valueOf)

        //thenAccept没有返回值，thenApply有返回值
        future1.thenApply(s -> "abc") //future1.thenApply(s -> {return "abc";})
                .thenAccept(s -> System.out.println(s))
                .thenApply(o -> String.valueOf(o));

        //返回了一个新的 CompletableFuture 对象，且类型为 String
        CompletableFuture<Void> f1 = future1.thenApply(s -> s + "abc")
                .thenAccept(s->System.out.println(s));

        future1.thenCompose(s->CompletableFuture.supplyAsync(()->1.1));

        future1.thenApply(CompletableFutureTest2::callServiceB);

        future1.thenApply(v -> 1.1)
               .thenApply(CompletableFutureTest2::callServiceB);

        Consumer<String> c = s -> System.out.println(s);
        //Consumer<String> c = System.out::println;
        c.accept("abc");
    }

    private static Double callServiceA(){
        try {
                System.out.println("service A is running...");
                Thread.sleep(4000);
                System.out.println("service A done.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1.1;
    }

    private static Integer callServiceB(Double d){
        try {
            Thread.sleep(5000);
            System.out.println("calling service B");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2;
    }
}
