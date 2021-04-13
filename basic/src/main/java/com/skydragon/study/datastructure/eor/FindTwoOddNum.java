package com.skydragon.study.datastructure.eor;

/*
题目2：数组中有2种数出现奇数次，且这2种数不相等；其余的数都出现偶数次，找到出现奇数次的那2种数
 */
public class FindTwoOddNum {
    public static void main(String args[]){
        int [] arr = new int[] {1,1,2,2,2,3,3,6,5,5};

        //得到eor=2^6
        int eor1 = 0;
        int eor2 = 0;
        for(int i = 0; i < arr.length; i++){
            eor1 ^= arr[i];
        }

        //提取eor最右侧的1
        //因为两种数不相等，必定存在某一位不相同，异或结果为1, 提取最右侧的1就可以了
        int rightOne = eor1 & (~eor1 + 1);

        for(int i = 0; i < arr.length; i++){
            if((arr[i] & rightOne) != 0){
                eor2 ^= arr[i];
            }
        }

        System.out.println(eor2);
        System.out.println((eor1^eor2));
    }
}
