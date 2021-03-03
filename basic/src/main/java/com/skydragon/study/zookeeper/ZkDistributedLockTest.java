package com.skydragon.study.zookeeper;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.locks.LockSupport;

public class ZkDistributedLockTest {
    static class ZKDistributedLock implements Watcher {
        private static final String CONNECT_STR = "192.168.75.130:2181";
        private static final String LOCK_ROOT = "DLock";

        static ZooKeeper zooKeeper;

        public ZKDistributedLock(){
            try {
                zooKeeper = new ZooKeeper(CONNECT_STR, 3000, new ZKDistributedLock());
                //创建根节点
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Thread thread;

        public boolean acquire(){
            //zooKeeper.create(LOCK_ROOT,"".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            return true;
        }

        public boolean release(){
            return true;
        }

        @Override
        public void process(WatchedEvent watchedEvent) {
            if(watchedEvent.getType() == Event.EventType.NodeDeleted){
                LockSupport.unpark(thread);
            }
        }
    }

    public static void main(String[] args) {

    }
}
