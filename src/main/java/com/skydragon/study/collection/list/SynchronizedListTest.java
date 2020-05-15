package com.skydragon.study.collection.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SynchronizedListTest {
    public static void main(String[] args){
        List list = Collections.synchronizedList(new ArrayList<>());
    }
}
