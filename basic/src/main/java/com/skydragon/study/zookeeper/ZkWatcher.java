package com.skydragon.study.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.locks.LockSupport;

public class ZkWatcher implements Watcher,AsyncCallback.DataCallback {
    private final ZooKeeper zooKeeper;
    Thread thread;

    public ZkWatcher(ZooKeeper zk){
        zooKeeper = zk;
    }

    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {

    }

    @Override
    public void process(WatchedEvent event) {
        if(event.getType() == Event.EventType.NodeDataChanged){
            try {
                byte[] data = zooKeeper.getData(event.getPath(),this,new Stat());
                String dataText = new String(data);
                System.out.println("node data changed to " + dataText);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(event.getType() == Event.EventType.NodeDeleted){
            LockSupport.unpark(this.thread);
        }
    }
}
