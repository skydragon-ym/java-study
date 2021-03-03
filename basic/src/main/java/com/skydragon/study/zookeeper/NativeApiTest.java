package com.skydragon.study.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class NativeApiTest {
    private static final String CONNECT_STR = "192.168.75.130:2181/java_study";

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        ZooKeeper zk = new ZooKeeper(CONNECT_STR, 3000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("Receive event " + event);
                if(Event.KeeperState.SyncConnected == event.getState())
                    System.out.println("connection is established...");
            }
        });

        ZkWatcher monitor = new ZkWatcher(zk);

        //Watcher is one time trigger
        zk.getData("/srv01", monitor, new Stat());

        Thread.sleep(1000);

        System.in.read();
    }

}
