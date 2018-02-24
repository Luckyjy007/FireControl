package org.chinalbs.main;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.chinalbs.kafka.KafkaConsumerConf;
import org.chinalbs.pb.MotObs;
import org.chinalbs.pb.MotObs.StObs;
import org.chinalbs.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


/*
Create by jiangyun on 2017/12/20
*/
public class Sparkmain {

    private static final Logger logger = LoggerFactory.getLogger(Sparkmain.class);


    public static void main(String[] args) {


        SparkConf sparkConf = new SparkConf().setAppName("chinalbs").setMaster("spark://172.169.0.89:7077").setJars(new String[]{"D:\\Work\\FireControl\\out\\artifacts\\mySpark\\mySpark.jar"});
        // SparkConf sparkConf = new SparkConf().setAppName("chinalbs").setMaster("spark://172.169.0.89:7077");
        sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
        JavaStreamingContext streamingContext = new JavaStreamingContext(sparkContext, Duration.apply(1000));
        String[] topics = Constant.TOPIC_1;
        JavaInputDStream<ConsumerRecord<String, byte[]>> directStream = KafkaUtils.createDirectStream(streamingContext, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(Arrays.asList(topics), KafkaConsumerConf.getKafkaParams()));
        streamingContext.checkpoint("/user/jiangyun/checkpoint");
        JavaDStream<ConsumerRecord<String, byte[]>> repartition = directStream.repartition(3);

        JavaDStream<String> map = repartition.map(new Function<ConsumerRecord<String, byte[]>, String>() {
            @Override
            public String call(ConsumerRecord<String, byte[]> v1) throws Exception {

                StObs stobs = MotObs.StObs.parseFrom(v1.value());
                String strSta = stobs.getStrSta();
                return strSta + "sfsdfsdfsdf";
            }
        });


        JavaDStream<String> transform = map.transform(new Function2<JavaRDD<String>, Time, JavaRDD<String>>() {
            @Override
            public JavaRDD<String> call(JavaRDD<String> v1, Time v2) throws Exception {

                v1.saveAsTextFile("hdfs://172.169.0.89:8020/Sparkttt");
                logger.warn("sssssssssssssssssssssss");
                return v1;
            }
        });

        transform.foreachRDD(new VoidFunction2<JavaRDD<String>, Time>() {
            @Override
            public void call(JavaRDD<String> v1, Time v2) throws Exception {
                v1.foreach(new VoidFunction<String>() {
                    @Override
                    public void call(String s) throws Exception {

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
