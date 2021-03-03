package com.skydragon.study.gc;

/*
vm option: -Xmx:20m -Xms:20m -XX:+PrintGCDetails
 */
public class GCOutOfMemory {
    public static void main(String[] args){
        GCOutOfMemory o = null;
        for(int i=0;i<1000000000;i++){
            o = new GCOutOfMemory();
        }
    }

}
