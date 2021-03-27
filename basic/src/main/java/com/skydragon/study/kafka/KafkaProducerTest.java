package com.skydragon.study.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;

//这里没有使用spring的方式配置kafka，使用的原生api
public class KafkaProducerTest {
    public static void main(String[] args){

        Properties p = new Properties();
        p.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.75.129:9092");
        //p.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Kafka)
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(p);
    }
}
