package com.skydragon.study.jmm;

/*
Cache Line size: 64字节
Long size: 8字节
定义一个数组，数组长度是要执行的线程数，每个线程分别读写数组中不同的元素
目前没有成功
 */
public final class FalseSharing implements Runnable {
    public static int NUM_THREADS = 2; // change
    public final static long ITERATIONS = 1_000_000_000L;
    private final int arrayIndex;
    private static VolatileLong[] longs;

    public FalseSharing(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception {
        //Thread.sleep(10000);
        System.out.println("starting....");
        if (args.length == 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }
        //初始化NUM_THREADS个元素的数组
        longs = new VolatileLong[NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("duration = " + (System.currentTimeMillis() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseSharing(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }

    public final static class VolatileLong extends Padding{
        public volatile long value = 0L;
    }

    public static class Padding{
        //public long p1, p2, p3, p4, p5, p6, p7;
        public long p1, p2, p3, p4, p5, p6;

        private Padding(){
            long l = p1+p2+p3+p4+p5+p6;
        }
    }
}
