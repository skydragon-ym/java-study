package com.skydragon.study.datastructure.unionfind;

import java.util.*;

/*
并查集，HashMap实现
 */
public class ProvinceHashMap {
    public static class Node<V> {
        V value;

        public Node(V v){
            value = v;
        }
    }

    public static class UnionFindSet<V> {
        //三个重要的容器
        public HashMap<V, Node<V>> nodes;
        public HashMap<Node<V>, Node<V>> parents;
        public HashMap<Node<V>, Integer> sizeMap;

        public UnionFindSet(List<V> list){
            nodes = new HashMap<>();
            parents = new HashMap<>();
            sizeMap = new HashMap<>();

            for(V item : list){
                Node<V> node = new Node<>(item);
                nodes.put(item, node);
                parents.put(node,node);
                sizeMap.put(node,1);
            }
        }

        //从给定节点出发，一路找到根节点
        public Node<V> findParent(Node<V> cur){
            Stack<Node<V>> stack = new Stack<>();

            while (cur != parents.get(cur)){
                stack.push(cur);
                cur = parents.get(cur);
            }

            //这里可以做一个优化，把上面遍历过得节点都挂在根节点下，减少每次查询parent时的链路长度
            if(!stack.isEmpty()){
                Node<V> node = stack.pop();
                parents.put(node,cur);
            }

            return cur;
        }

        public boolean isSameSet(V x, V y){
            return findParent(nodes.get(x)) == findParent(nodes.get(y));
        }

        public void union(V x, V y){
            Node<V> xhead = findParent(nodes.get(x));
            Node<V> yhead = findParent(nodes.get(y));

            int xSetSize = sizeMap.get(xhead);
            int ySetSize = sizeMap.get(yhead);

            //分别找到2个集合的代表节点，没有使用if语句
            Node<V> big = xSetSize >= ySetSize ? xhead : yhead;
            Node<V> small = big == xhead ? yhead : xhead;

            //把元素少的集合的代表节点挂到元素多的集合的代表节点
            parents.put(small,big);

            //增加合并后的元素个数
            sizeMap.put(big, xSetSize + ySetSize);

            //删除元素的集合的代表节点的SizeMap
            sizeMap.remove(small);

        }
    }

    public static void main(String[] args){
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
        UnionFindSet<Integer> ufs = new UnionFindSet<>(list);

        System.out.println(ufs.isSameSet(1,2));
        ufs.union(1,2);
        System.out.println(ufs.isSameSet(1,2));
    }

}
