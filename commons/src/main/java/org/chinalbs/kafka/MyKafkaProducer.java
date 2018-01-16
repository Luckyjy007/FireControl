package org.chinalbs.kafka;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.chinalbs.mockdate.GenerateData;
import org.chinalbs.tablemodel.Alarm;

/*
Create by jiangyun on 2017/12/20
*/
public class MyKafkaProducer {
    public static void main(String[] args) {
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(KafkaProducerConf.getKafkaParams());
        String topic = "Hdfs";
        GenerateData generateData = new GenerateData();
        while (true) {
            Alarm alarm = generateData.GenerateAlarmData();
            sendMessage(topic, alarm, producer);
            try {

                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private static void sendMessage(String topic, Object object, KafkaProducer producer) {

        ProducerRecord<String, String> producerRecord = new ProducerRecord<String, String>(topic, JSON.toJSONString(object));
        producer.send(producerRecord);

    }


}
