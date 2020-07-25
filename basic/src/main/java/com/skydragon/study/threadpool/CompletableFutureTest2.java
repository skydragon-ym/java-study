package com.skydragon.study.threadpool;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CompletableFutureTest2 {
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Double> future1 = CompletableFuture.supplyAsync(CompletableFutureTest2::callServiceA);
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(()->callServiceB());

        future1.whenComplete((s,throwable) ->{
            System.out.println("future1 task done.");
        });

        future1.whenComplete(new BiConsumer<Double, Throwable>() {
            @Override
            public void accept(Double t, Throwable action) {
                System.out.println("future1 task done");
            }
        });

        future1.join();
        //CompletableFuture.allOf(future1,future2).join();

        //调用函数式接口
        future1.thenAccept(s -> System.out.println(s)) //thenAccept(System.out::println)
                .thenApply(i -> String.valueOf(i)); //thenApply(String::valueOf)

        //future1.thenApply(CompletableFutureTest2::callServiceB);
        //Consumer<String> c = s -> System.out.println(s);
        //Consumer<String> c = System.out::println;
        //c.accept("abc");
    }

    private static Double callServiceA(){
        try {
            Thread.sleep(4000);
            System.out.println("calling service A");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 1.1;
    }

    private static Integer callServiceB(){
        try {
            Thread.sleep(5000);
            System.out.println("calling service B");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 2;
    }
}
