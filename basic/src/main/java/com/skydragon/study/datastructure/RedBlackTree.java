package com.skydragon.study.datastructure;

public class RedBlackTree {

    static class MyClass1{
        static final int a = 1;
        static int b = 2;
        static {
            System.out.println("MyClass1 loaded...");
        }
    }

    static class MyClass2 extends MyClass1{
    }

    public static void main(String[] args){
        //只有
        MyClass1 c1;
        int a = MyClass1.a;
        int b = MyClass1.b;
        //MyClass2 c2 = new MyClass2();
    }
}
