package com.skydragon.study.threadpool;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallbackTest {
    public interface ICallback {
        public void callback(Map<String, Object> params);
    }

    public static void doStm(final ICallback callback) {
        // 初始化一个线程
        Thread t = new Thread() {
            public void run() {

                // 这里是业务逻辑处理
                System.out.println("do sth...");

                // 为了能看出效果 ，让当前线程阻塞5秒
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 处理完业务逻辑，
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("a1", "这是我返回的参数字符串...");
                callback.callback(params);
            };
        };

        es.execute(t);
    }

    static ExecutorService es = Executors.newFixedThreadPool(2);
    public static void main(String[] args){
        // 内部类 等价于 ICallback callbck = new ICallback的实现类();
        // 新手如果对内部类的写法有疑惑可以查看一下编译后的class文件或许能明白
        // 在编译的时候会把内部类编译成一个实现了ICallback的class
        doStm(new ICallback() {
            /**
             * 现在这个方法是doStm结束后调用<br>
             * 结束之后会主动把处理参数params传递过来
             */
            @Override
            public void callback(Map<String, Object> params) {
                System.out.println("单个线程也已经处理完毕了，返回参数a1=" + params.get("a1"));
            }
        });

        System.out.println("主线任务已经执行完了");
        es.shutdown();
    }
}
