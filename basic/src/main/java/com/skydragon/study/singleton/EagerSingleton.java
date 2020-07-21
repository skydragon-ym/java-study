package com.skydragon.study.singleton;

/*
Eager Initialize (饿汉模式)
JVM保证static只会加载一次，所以是单例
 */
public class EagerSingleton {
    private static final EagerSingleton INSTANCE = new EagerSingleton();

    private EagerSingleton() {}

    public static EagerSingleton getInstance(){
        return INSTANCE;
    }

    public static void main(String[] args){
        EagerSingleton obj1 = EagerSingleton.getInstance();
        EagerSingleton obj2 = EagerSingleton.getInstance();
        System.out.println(obj1 == obj2);
    }
}

