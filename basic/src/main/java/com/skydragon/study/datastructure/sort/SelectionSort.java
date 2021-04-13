package com.skydragon.study.datastructure.sort;

import java.util.Arrays;

/*
选择排序
 */
public class SelectionSort {
    public static void main(String args[]){
        int [] arr = new int[] {6,5,4,7,3,2,1};

        //0 - N-1
        //1 - N-1
        //2 - N-1
        for(int i=0; i<arr.length-1; i++){
            int minIndex = i;
            for(int j=i+1; j<arr.length; j++){
                minIndex = arr[j] < arr[minIndex] ? j : minIndex;
            }
            swap(arr,i,minIndex);
            System.out.println(Arrays.toString(arr));
        }

        //System.out.println(Arrays.toString(arr));
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}
