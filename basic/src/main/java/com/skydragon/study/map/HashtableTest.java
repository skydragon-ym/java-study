package com.skydragon.study.map;

import sun.security.pkcs11.wrapper.Constants;

import java.util.Hashtable;
import java.util.UUID;

public class HashtableTest {
    static int count = 1000000;
    static UUID[] keys = new UUID[count];
    static UUID[] values = new UUID[count];

    static {
        for(int i=0;i<count;i++){
            keys[i] = UUID.randomUUID();
            values[i] = UUID.randomUUID();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Hashtable<UUID, UUID> ht = new Hashtable<>();
        for(int i=0; i<100; i++){
            int finalI = i;
            Thread t = new Thread(()->{
                for(int j=0;j<10000;j++) {
                    int startIndex = finalI * 10000 + j;
                    ht.put(keys[startIndex], values[startIndex]);
                }
            });
            t.start();
            t.join();
        }

        long end = System.currentTimeMillis();
        System.out.println(end-start);
        System.out.println(ht.size());

        start = System.currentTimeMillis();
        for(int i=0; i<100; i++){
            int finalI = i;
            Thread t = new Thread(()->{
                for(int j=0;j<10000000;j++) {
                    ht.get(keys[10]);
                }
            });
            t.start();
            t.join();
        }
        end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
