package com.skydragon.study.collection.queue;

import java.util.PriorityQueue;

public class PriorityQueueTest {
    public static void main(String[] args){
        PriorityQueue<String> q = new PriorityQueue<>();
        q.add("d");
        q.add("c");
        q.add("b");
        q.add("a");

        for(int i=0; i<4; i++){
            String s = q.poll();
            System.out.println(s);
        }
    }
}
