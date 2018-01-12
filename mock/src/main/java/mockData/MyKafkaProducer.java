package mockData;


import Bean.Alarm;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class MyKafkaProducer {

    KafkaProducer<String, String> producer;
    GenerateData generateData = new GenerateData();

    public KafkaProducer<String, String> init() {
        Properties props = new Properties();
        // Kafka服务端的主机名和端口号
        props.put("bootstrap.servers", "cors-01:9092");
        // 等待所有副本节点的应答
        props.put("acks", "1");
        // 消息发送最大尝试次数
        props.put("retries", 3);
        // 一批消息处理大小
        props.put("batch.size", 16384);
        // 请求延时
        props.put("linger.ms", 10);
        // 发送缓存区内存大小
        props.put("buffer.memory", 33554432);
        // key序列化
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value序列化
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
        return producer;
    }

    public void sendMessage() {
        Alarm alarm = generateData.GenerateAlarmData();
        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>("AlarmRealTime", alarm.toString());
        producer.send(producerRecord);

    }

    public static void main(String[] args) {
        MyKafkaProducer myKafkaProducer = new MyKafkaProducer();
        myKafkaProducer.init();
        while (true) {
            myKafkaProducer.sendMessage();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
