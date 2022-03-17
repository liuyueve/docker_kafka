package com.voi.docker.kafka_demo.quickstart;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * @author : liu·yu
 * date : 12/21/21
 */
public class ConsumerFastStart {

    public static final String groupId = "group.demo";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Constant.BROKER_LIST);

        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        //订阅主题
        consumer.subscribe(Collections.singletonList(Constant.TOPIC));
        //循环消费消息
        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(5000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println("========================================");
                System.out.println(record.value());
                System.out.println(record.topic());
                System.out.println(record.partition());
                System.out.println(record.offset());
                System.out.println("========================================");
            }
        }
    }
}
