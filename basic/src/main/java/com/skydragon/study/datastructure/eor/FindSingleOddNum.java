package com.skydragon.study.datastructure.eor;

/*
题目1：数组中只有1个数字有奇数个，其余数字全是偶数个，找到奇数个的那个数字
 */
public class FindSingleOddNum {
    public static void main(String[] args){
        int [] arr = new int[] {1,1,2,2,2,3,3};
        int eor = 0;
        int index = 0;
        while(index < arr.length){
            eor ^= arr[index];
            index++;
        }

        System.out.println(eor);
    }
}
