package org.chinalbs.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.chinalbs.utils.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*
Create by jiangyun on 2017/12/19
*/
public class HbaseConf {

    private static final Logger logger = LoggerFactory.getLogger(HbaseConf.class);
    private static Map instance = null;
    private static Configuration conf = null;

    private static synchronized Map init() {
        if (instance == null) {
            instance = new HashMap<String, String>(10);
        }
        InputStream inputStream = HbaseConf.class.getClassLoader().getResourceAsStream(Constant.HBASE_PROP);
        Properties p = new Properties();
        try {
            p.load(inputStream);
        } catch (IOException e) {
            //  e.printStackTrace();
            logger.error("Error loading configuration file information");
            throw new RuntimeException(e);
        }
        instance.putAll(p);
        return instance;
    }

    public static synchronized Configuration get() {
        init();
        if (conf == null) {
            conf = HBaseConfiguration.create();
            conf.set(Constant.HBASE_ZOOKPPER_PORT, instance.get(Constant.HBASE_ZOOKPPER_PORT).toString());
            conf.set(Constant.HBASE_NODE, instance.get(Constant.HBASE_NODE).toString());
        }
        return conf;
    }


}
