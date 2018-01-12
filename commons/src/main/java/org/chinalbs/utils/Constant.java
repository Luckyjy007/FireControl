package org.chinalbs.utils;

/*
Create by jiangyun on 2017/12/19
*/

import java.math.BigDecimal;
import java.math.BigInteger;

//常量参数
public class Constant {

    public final static String HBASE_PROP = "hbase.properties";
    public final static String HBASE_ZOOKPPER_PORT = "hbase.zookeeper.property.clientPort";
    public final static String HBASE_NODE = "hbase.zookeeper.quorum";
    public final static String KAFKAPRODUCER_PROP= "kafkaproducer.properties";
    public final static String KAFKACOMSUMEER_PROP ="kafkaconsumer.properties";
    public final static String[] TOPIC_1={"Hdfs","test2","test3","test4","TestAlarm"};

    //Hbase TableName about FireControl

    //organization about 17176523678923 this means that 1717 beijingshi 65 haidanqu 23 University
    //6789 organizationCode 23 AlarmTable
    public final static String TsinghuaUniversityAlarm = "17176523678923";
    public final static String TsinghuaUniversityCount = "17176523678954";
    public final static String TsinghuaUniversityOther = "17176523678912";


    public final static String[] ColumnFamily = {"info"};


    //HDFSURI
    public final static String URI = "hdfs://172.169.0.89:8020";
    //public final static String URI = "hdfs:// 192.168.1.101:8020";
}
