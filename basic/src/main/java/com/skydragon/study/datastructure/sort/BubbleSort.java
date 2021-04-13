package com.skydragon.study.datastructure.sort;

import java.util.Arrays;

public class BubbleSort {
    public static void main(String[] args){
        int [] arr = new int[] {6,5,4,7,3,2,1};

        //0 - N-1
        //0 - N-2
        //0 - N-3
        for(int e=arr.length-1; e>0; e--){
            for(int i=0; i<e; i++){
                if(arr[i] > arr[i+1]){
                    swap(arr,i,i+1);
                }
            }
        }

        //System.out.println(Arrays.toString(arr));
    }

    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
