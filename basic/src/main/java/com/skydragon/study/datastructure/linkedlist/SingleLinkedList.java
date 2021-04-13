package com.skydragon.study.datastructure.linkedlist;

public class SingleLinkedList {

    //单链表节点
    public class Node{
        int value;
        Node next;
    }

    public Node reverse(Node head){
        Node newHead = head;



        return head;
    }

    public Node create(int[] arr){
        //返回空链表
        if(arr.length == 0){
            return null;
        }

        Node head = new Node();
        Node newNode = null;

        for(int num : arr){
            newNode = new Node();
            newNode.value = num;

            //链表头插
            newNode.next = head.next;
            head.next = newNode;
        }

        return head;
    }

    public static void main(String[] args){
        SingleLinkedList linkedList = new SingleLinkedList();
        Node head = linkedList.create(new int[]{1,2,3,4,5,6});

    }
}


