package com.skydragon.study.collection.list;

import java.util.ArrayList;
import java.util.List;

class ArrayListTest {
    public static void main(String[] args){
        List list = new ArrayList();

        //add
        long start = System.currentTimeMillis();
        for(int i=1;i<400000;i++){
            list.add(0,i);
        }
        long end = System.currentTimeMillis();
        System.out.println(end-start);

        //get
        start = System.currentTimeMillis();
        for(int i=0;i<10000000;i++){
            list.get(100);
        }
        end = System.currentTimeMillis();
        System.out.println(end-start);
    }
}
