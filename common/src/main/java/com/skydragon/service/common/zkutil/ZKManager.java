package com.skydragon.service.common.zkutil;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooKeeper;

public class ZKManager {
    //connect to zookeeper leader
    private static final String CONNECT_STR = "192.168.75.130:2181";

    CuratorFramework client = null;

    public void ZKManager(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder()
                        .connectString(CONNECT_STR)
                        .sessionTimeoutMs(5000)
                        .connectionTimeoutMs(5000)
                        .retryPolicy(retryPolicy)
                        .namespace("java_study")
                        .build();

        client.start();
    }


}
