package com.skydragon.study.synchronize;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

public class Biasedlock {
    public static void main(String[] args) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Biasedlock object = new Biasedlock();
        System.out.println(ClassLayout.parseInstance(object).toPrintable());
    }
}
