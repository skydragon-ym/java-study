package com.skydragon.study.jmm;

/*
Cache Line size: 64字节
Long size: 8字节
这个例子没有用数组，而是用了2个对象
目前没有成功
 */
public class FalseSharing2 {
    static final long iteration = 5_000_000_000L;
    /*
    static class Data extends Padding{
        long x = 1L;
        long y = 2L;
    }
    */
    static class Data1 extends Padding{
       volatile long x = 1L;

       private Data1(){
           long l = p1+p2+p3+p4+p5+p6+p7;
       }
    }

    static class Data2 extends Padding{
        volatile long y = 2L;

        private Data2(){
            long l = p1+p2+p3+p4+p5+p6+p7;
        }
    }

    public static class Padding{
        public long p1, p2, p3, p4, p5, p6, p7;
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("starting....");

        Data1 data1 = new Data1();
        Data2 data2 = new Data2();

        Thread t1 = new Thread(()->{
            for(long i = 1; i<=iteration;i++){
                data1.x = i;
            }
        });

        Thread t2 = new Thread(()->{
            for(long i = 1; i<=iteration;i++){
                data2.y = i;
            }
        });

        final long start = System.currentTimeMillis();
        t1.start();
        t1.join();

        t2.start();
        t2.join();
        System.out.println("duration = " + (System.currentTimeMillis() - start));
    }
}
