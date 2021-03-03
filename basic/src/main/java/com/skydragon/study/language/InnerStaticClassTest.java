package com.skydragon.study.language;

public class InnerStaticClassTest {
    static class MyInnerStaticClass{
        public void test(){
            System.out.println("hello");
        }
    }
    public static void main(String[] args){
        InnerStaticClassTest.MyInnerStaticClass o = new InnerStaticClassTest.MyInnerStaticClass();
        o.test();
    }
}
