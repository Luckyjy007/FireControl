package org.chinalbs.main;

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
import org.chinalbs.hdfs.HdfsUtils;
import org.chinalbs.kafka.KafkaConsumerConf;
import org.chinalbs.utils.Constant;
import org.apache.spark.api.java.function.VoidFunction2;

import java.util.Arrays;
import java.util.Iterator;


/*
Create by jiangyun on 2017/12/20
*/
public class Sparkmain {
    private static long beginTime = System.currentTimeMillis();

    static {
        beginTime = System.currentTimeMillis();
    }


    public static void main(String[] args) {
       /* try {
            HBaseUtil.createTable(Constant.TsinghuaUniversityOther, Constant.ColumnFamily, false);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        SparkConf sparkConf = new SparkConf().setAppName("chinalbs").setMaster("local[*]");
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, Duration.apply(1000));
        String[] topics = Constant.TOPIC_1;
        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(Arrays.asList(topics), KafkaConsumerConf.getKafkaParams()));
        streamingContext.checkpoint("/user/jiangyun/checkpoint");
        JavaDStream<ConsumerRecord<String, String>> repartition = directStream.repartition(1);
        String uri = "/user/hbase/jiangyun";
        repartition.foreachRDD(new VoidFunction2<JavaRDD<ConsumerRecord<String, String>>, Time>() {
            @Override
            public void call(JavaRDD<ConsumerRecord<String, String>> v1, Time v2) throws Exception {
                v1.foreachPartition(new VoidFunction<Iterator<ConsumerRecord<String, String>>>() {
                    int i=0;
                    String path = uri;
                    @Override
                    public void call(Iterator<ConsumerRecord<String, String>> consumerRecordIterator) throws Exception {

                        while (consumerRecordIterator.hasNext()) {

                          /*  JSONObject jsonObject = JSON.parseObject(consumerRecordIterator.next().value());
                            Alarm alarm = JSON.toJavaObject(jsonObject, Alarm.class);
                            Put put = HBaseUtil.objectToPut(alarm, Constant.ColumnFamily);
                            HBaseUtil.put(Constant.TsinghuaUniversityOther, put);*/

                            ConsumerRecord<String, String> consumerRecord = consumerRecordIterator.next();

                           if (HdfsUtils.getFile(path).getLen()>=256*1024){
                               i++;
                               path = uri+i;
                               HdfsUtils.append(uri +i, consumerRecord.toString() + "\n");
                           }
                           else {
                               HdfsUtils.append(path , consumerRecord.toString() + "\n");
                           }


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
