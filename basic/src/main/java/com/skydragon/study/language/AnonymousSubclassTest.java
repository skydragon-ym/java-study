package com.skydragon.study.language;

interface BackgroundTask {
    void doBackgroundTask();
}

public class AnonymousSubclassTest implements BackgroundTask {

    @Override
    public void doBackgroundTask() {
        System.out.println("doing background task");
    }

    public static void main(String[] args){
        AnonymousSubclassTest obj = new AnonymousSubclassTest(){
            @Override
            public void doBackgroundTask() {
                super.doBackgroundTask();
            }
        };

        obj.doBackgroundTask();
    }
}

