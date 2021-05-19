package com.skydragon.study.datastructure.graph;

/*
查找朋友圈个数, DFS遍历
 */
public class ConnectedComponent {
    static boolean[] visited;
    static int[][] arr;
    static int N;

    public static void main(String[] args){
        int[][] arr = {{1,1,0},{1,1,0},{0,0,1}};
        int result = findCircleNum(arr);

        System.out.println(result);
    }

    public static int findCircleNum(int[][] isConnected) {
        arr = isConnected;
        N = isConnected.length;
        visited = new boolean[N];
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (!visited[i]) {
                dfs(i);
                count++;
            }
        }
        return count;
    }

    private static void dfs(int index){
        if(visited[index] == true){
            return;
        }
        visited[index] = true;

        for(int i=0; i<N; i++){
            if(arr[index][i] == 1){
                dfs(i);
            }
        }
    }
}
