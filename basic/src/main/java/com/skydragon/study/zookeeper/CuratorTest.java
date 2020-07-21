package com.skydragon.study.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class CuratorTest {
    private static final String CONNECT_STR = "192.168.75.130:2181";

    public static void main(String[] args) throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

        CuratorFramework client =
                CuratorFrameworkFactory.builder()
                        .connectString(CONNECT_STR)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .namespace("java_study")
                        .build();

        client.start();

        //sync
        //client.create().forPath("srv01", "service01 data".getBytes());

        //async
        /*

        client.create().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                String path = event.getPath();
                System.out.println("callback thread:" + Thread.currentThread().getId() +
                        " Name: " + event.getName() +
                        " Path: " + event.getPath()
                );
            }
        }).forPath("/srv02", "service02 data".getBytes());
        System.out.println("main thread:" + Thread.currentThread().getId());
        */

        //什么类型的操作可以添加watch? get, check exist
        client.getData().usingWatcher(new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if(event.getType() == Event.EventType.NodeDataChanged){
                    System.out.println("Node Data Changed.");
                }
            }
        }).inBackground().forPath("/srv02");

        System.in.read();
    }
}
