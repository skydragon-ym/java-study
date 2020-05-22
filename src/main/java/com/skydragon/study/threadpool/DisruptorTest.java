package com.skydragon.study.threadpool;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class DisruptorTest {

    public static void main(String[] args){
        class Event {
            private long value;

            public void setValue(long value) {
                this.value = value;
            }
        }

        class MyEventHandler implements EventHandler<Event>{
            @Override
            public void onEvent(Event event, long l, boolean b) throws Exception {
                System.out.println(event.value);
            }
        }

        EventFactory<Event> eventFactory = new EventFactory<Event>() {
            @Override
            public Event newInstance() {
                return new Event();
            }
        };

        EventTranslator<Event> eventTranslator = new EventTranslator<Event>() {
            @Override
            public void translateTo(Event event, long l) {
                event.setValue(l+1);
            }
        };

        Disruptor disruptor = new Disruptor(eventFactory, 8, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());

        disruptor.handleEventsWith(new MyEventHandler());

        RingBuffer<Event> rb = disruptor.start();

        //producer
        for(int i=0;i<20;i++){
            rb.publishEvent(eventTranslator);
        }

        disruptor.shutdown();

    }
}
