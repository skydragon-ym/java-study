package com.skydragon.study.datastructure.linkedlist;

public class SkipListTest {
    private final double SKIPLIST_P = 0.25;
    private final int MAX_LEVEL = 3;

    public static void main(String[] args){
        SkipListTest app = new SkipListTest();

        for(int i=0;i<20;i++){
            int result = app.randomLevel();
            System.out.println(result);
        }

    }

    /*
    随机造层
     */
    private int randomLevel() {
        int level = 1;
        // 当 level < MAX_LEVEL，且随机数小于设定的晋升概率时，level + 1
        while (Math.random() < SKIPLIST_P && level < MAX_LEVEL)
            level += 1;
        return level;
    }
}
