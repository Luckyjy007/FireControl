package mockData;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import scala.tools.cmd.gen.AnyVals;

import java.util.Arrays;
import java.util.Properties;

public class MyConsumer {

    public KafkaConsumer init() {
        Properties properties = new Properties();
        // 定义kakfa 服务的地址，不需要将所有broker指定上
        properties.put("bootstrap.servers", "hadoop102:9092");
        // 制定consumer group
        properties.put("group.id", "test");
        // 是否自动确认offset
        properties.put("enable.auto.commit", "true");
        // 自动确认offset的时间间隔
        properties.put("auto.commit.interval.ms", "1000");
        // key的序列化类
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // value的序列化类
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 定义consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // 消费者订阅的topic, 可同时订阅多个
        consumer.subscribe(Arrays.asList("AlarmRealTime"));
        return consumer;


    }

    public static void main(String[] args) {
        MyConsumer consumer = new MyConsumer();
        KafkaConsumer<String, String> kafkaConsumer = consumer.init();
        while (true) {
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
            for (ConsumerRecord consumerRecord : consumerRecords) {
                System.out.println("offset: " + consumerRecord.offset() + " topic: " + consumerRecord.topic()
                        + " value: " + consumerRecord.value() + " key: " + " toString: " + consumerRecord.toString() + consumerRecord.key() + " partition: " + consumerRecord.partition()
                );
            }

        }
    }

}
