package sparkStreaming;

import Bean.Alarm;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import scala.Serializable;
import scala.Tuple2;


import java.util.*;


public class FromKafka implements Serializable {
    static int i = 0;

    public static void main(String[] args) {

        String brokers = "cors-01:9092";
        String topics = "AlarmRealTime";
        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("streaming word count");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new SQLContext(sc);
        sc.setLogLevel("WARN");
        JavaStreamingContext ssc = new JavaStreamingContext(sc, Durations.seconds(1));


        Collection<String> topicsSet = new HashSet<>(Arrays.asList(topics.split(",")));
        String sourcetopic = "AlarmRealTime";
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", brokers);
        kafkaParams.put("group.id", "fireControlSparkStreaming");
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", "false");
        kafkaParams.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaParams.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        //Topic分区
        JavaInputDStream<ConsumerRecord<String, String>> directStream = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent(), ConsumerStrategies.Subscribe(Arrays.asList(sourcetopic), kafkaParams));
        String tableName = "AlarmRealTime";
        String cfs = "info";

/*
        try {
            HBaseUtil.createTable(tableName,cfs,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
*/

        JavaDStream<String> stringJavaDStream = directStream.map(new Function<ConsumerRecord<String, String>, String>(

        ) {
            @Override
            public String call(ConsumerRecord<String, String> v1) throws Exception {

                String split = v1.value().split("\\{")[1];
                String s = split.split("\\}")[0];
                return s;
            }
        });

        JavaDStream<String> filter = stringJavaDStream.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String v1) throws Exception {

              /*  String s = v1.trim().split(", ")[7];
                String s1 = s.trim().split("=")[1];
                int i = Integer.parseInt(s1.trim());
                if (i == 3) {
                    return true;
                } else {
                    return false;
                }
                // return i == 3;*/

                return true;

            }
        });

        JavaPairDStream<String, String> stringStringJavaPairDStream = filter.mapToPair(new PairFunction<String, String, String>() {
            @Override
            public Tuple2<String, String> call(String s) throws Exception {
                String s1 = s.split(", ")[3];
                String s2 = s1.split("=")[1];
                return new Tuple2<String, String>(s2, s);
            }
        });
        JavaDStream<Alarm> alarmJavaDStream = stringStringJavaPairDStream.map(new Function<Tuple2<String, String>, Alarm>() {
            @Override
            public Alarm call(Tuple2<String, String> v1) throws Exception {

                String[] split = v1._2.split(", ");

                int primaryKey = Integer.parseInt(split[0].split("=")[1].trim());//主键
                String equipmentId = split[1].split("=")[1].trim();//设备编号
                String warehousingTime = split[2].split("=")[1].trim();//入库时间
                String alarmType = split[3].split("=")[1].trim();//报警类型
                Long alarmTime = Long.parseLong(split[4].split("=")[1].trim());//报警时间
                int artificialState = Integer.parseInt(split[5].split("=")[1].trim());//人工状态
                int systemStatus = Integer.parseInt(split[6].split("=")[1].trim());//系统状态
                int webReset = Integer.parseInt(split[7].split("=")[1].trim());//Web复位
                int state = Integer.parseInt(split[8].split("=")[1].trim());//状态
                String extra = split[9].split("=")[1].trim();//其他

                return new Alarm(primaryKey, equipmentId, warehousingTime, alarmType, alarmTime, artificialState, systemStatus, webReset, state, extra);
            }
        });


        alarmJavaDStream.foreachRDD(new VoidFunction2<JavaRDD<Alarm>, Time>() {
            @Override
            public void call(JavaRDD<Alarm> v1, Time v2) throws Exception {

                System.out.println(i);
                v1.foreach(new VoidFunction<Alarm>() {
                    @Override
                    public void call(Alarm alarm) throws Exception {
                        Table table = HBaseUtil.getTable(tableName);

                        Put put = HBaseUtil.objectToPut(alarm, tableName, cfs);
                        table.put(put);
                        HBaseUtil.put(tableName, put);
                        System.out.println("put data into hbase successfully");

                    }
                });
                i++;
                System.out.println(i);
                v1.saveAsTextFile("/user/hbase" + i);

            }
        });


        ssc.start();
        try {
            ssc.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
