package org.chinalbs.spark;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hbase.client.Put;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.chinalbs.hbase.HBaseUtil;
import org.chinalbs.kafka.KafkaConsumerConf;
import org.chinalbs.tablemodel.Alarm;
import org.chinalbs.utils.Constant;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/*
Create by jiangyun on 2017/12/20
*/
public class SparkStreaming {
    public static void main(String[] args) {
        try {
            HBaseUtil.createTable(Constant.TsinghuaUniversityAlarm, Constant.ColumnFamily, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SparkConf sparkConf = new SparkConf().setAppName("chinalbs").setMaster("local[*]");
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, Duration.apply(1000));
        String[] topics = Constant.TOPIC_1;
        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(Arrays.asList(topics), KafkaConsumerConf.getKafkaParams()));
        streamingContext.checkpoint("/user/jiangyun/checkpoint");
        JavaDStream<ConsumerRecord<String, String>> repartition = directStream.repartition(10);

        repartition.foreachRDD(new VoidFunction2<JavaRDD<ConsumerRecord<String, String>>, Time>() {
            @Override
            public void call(JavaRDD<ConsumerRecord<String, String>> v1, Time v2) throws Exception {
                v1.foreachPartition(new VoidFunction<Iterator<ConsumerRecord<String, String>>>() {
                    @Override
                    public void call(Iterator<ConsumerRecord<String, String>> consumerRecordIterator) throws Exception {
                        List<Put> puts = new ArrayList<Put>(100);

                        while (consumerRecordIterator.hasNext()) {

                            JSONObject jsonObject = JSON.parseObject(consumerRecordIterator.next().value());
                            Alarm alarm = JSON.toJavaObject(jsonObject, Alarm.class);
                            Put put = HBaseUtil.objectToPut(alarm, Constant.ColumnFamily);
                            puts.add(put);
                            System.out.println("successfuly");

                        }
                        while (null != puts) {
                            HBaseUtil.put(Constant.TsinghuaUniversityAlarm, puts);

                        }

                    }
                });
            }
        });


        streamingContext.start();
        try {
            streamingContext.awaitTermination();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        streamingContext.close();


    }


}
