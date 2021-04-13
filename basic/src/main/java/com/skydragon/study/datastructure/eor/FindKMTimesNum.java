package com.skydragon.study.datastructure.eor;

import com.skydragon.study.map.HashtableTest;

import java.util.HashMap;
import java.util.Hashtable;

/*
数组中有一种数出现K次，其他数都出现了M次，M > 1, K < M，找到出现了K次的数；
如果没有这个出现K次的数，返回-1
要求额外空间复杂度O(1), 意思是不能用哈希表
 */
public class FindKMTimesNum {
    public static void main(String[] args) {
        //int[] arr = new int[]{5, 5, 5, 0, 0, 1, 1, 1};
        int[] arr = new int[]{0,0,0};

        //有1个数出现了2次
        int k = 2;
        //其余的数都出现了3次
        int m = 3;

        int value = onlyKTimes(arr, k, m);

        System.out.println(value);

    }

    public static int onlyKTimes(int[] arr, int k, int m) {
        /*
        t[0]: 0位置的1出现了几个
        t[i]: i位置的1出现了几个
         */
        int[] t = new int[32];

        for (int num : arr) {
            for (int i = 0; i < 32; i++) {
                if (((num >> i) & 1) != 0) {
                    t[i]++;
                }
            }
        }

        int value = 0;
        for (int i = 0; i < 32; i++) {
            //如果e不能被m整除，说明这里面有出现k次的那个数在这个位置上为1，否则为0

            if(t[i] % m == 0){
                continue;
            }
            if(t[i] % m == k){
                value |= (1 << i);
            }
            else{
                return -1;
            }

            /*
            if (t[i] % m != 0 ) {
                value |= (1 << i);
            }
             */
        }

        return value;
    }

    public static int test(int[] arr, int k, int m) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            if (map.containsKey(num)) {
                map.put(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        for (int num : map.keySet()) {
            if (map.get(num) == k) {
                return num;
            }
        }

        return -1;
    }
}
