package com.skydragon.study.singleton;
/*
Lazy Initialize (懒汉模式)
加锁会降低效率
 */
public class LazySingleton {
    private static volatile LazySingleton INSTANCE;

    private LazySingleton(){}

    /*
    public static synchronized LazySingleton getInstance() throws InterruptedException {
        if(INSTANCE == null) {
            Thread.sleep(5);
            INSTANCE = new LazySingleton();
        }
        return INSTANCE;
    }
     */

    //锁细化，需要加锁的地方才加锁，不要加在整个方法上
    public static LazySingleton getInstance() throws InterruptedException {
        //这里可以有业务逻辑，不需要锁
        if(INSTANCE == null) {
            synchronized (LazySingleton.class) {
                if(INSTANCE ==null) {
                    Thread.sleep(5);
                    INSTANCE = new LazySingleton();
                }
            }
        }
        return INSTANCE;
    }

    public static void main(String[] args){
        for(int i=1; i<=100; i++){
            new Thread(()->{
                LazySingleton obj = null;
                try {
                    obj = LazySingleton.getInstance();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(obj.hashCode());
            }).start();
        }
    }
}
