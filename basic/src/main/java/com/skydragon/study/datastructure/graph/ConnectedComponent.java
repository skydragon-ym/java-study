package com.skydragon.study.datastructure.graph;

public class ConnectedComponent {
    static final int N = 3;
    static final int[][] arr = {{1,1,0},{1,1,0},{0,0,1}};

    static int[] visit = new int[N];

    public static void main(String[] args){
        int count = 0;
        for(int i=0; i<N; i++){
            if(visit[i] != 1) {
                dfs(i);
                count++;
            }
        }
        System.out.println(count);
    }

    private static void dfs(int index){
        for(int i=0; i<N-1; i++){
            visit[index] = 1;
            if(visit[i] != 1 && arr[index][i] != 0){
                dfs(i);
            }
        }
    }
}
