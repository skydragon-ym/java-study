package com.skydragon.study.collection.list;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class VectorTest {
    public static void main(String[] args){
        Vector<String> tickets = new Vector<>(100);
        for(int i=0;i<1000;i++){
            tickets.add("票 编号：" + i);
        }

        for(int i=0;i<10;i++){
            new Thread(()->{
                synchronized (tickets) {
                    while (tickets.size() > 0) {
                        try {
                            TimeUnit.MILLISECONDS.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(tickets.remove(0));
                    }
                }
            }).start();
        }
    }
}
