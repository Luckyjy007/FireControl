package org.chinalbs.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ConfigurationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
Create by jiangyun on 2018/1/12
*/
public class MongoPool {
    private static final Logger logger = LoggerFactory.getLogger(MongoPool.class);

    // host
    private static String host;
    // port
    private static Integer port;
    // username
    private static String userName;
    // password
    private static String password;
    // database name
    private static String dbName;
    // connected number of one host
    private static Integer connectionsNumbersPerHost;
    // thread number
    private static Integer threadsAllowedToBlockForConnectionMultiplier;
    // max wait time
    private static Integer maxWaitTime;
    // connect timeout
    private static Integer connectTimeout;
    // socket timeout
    private static Integer socketTimeout;


    private static Map<String, String> conf;
    private static MongoClient mongoClient;

    static {

        try {
            conf = MongoConf.getConf();
            host = conf.get("host");
            port = Integer.parseInt(conf.get("port"));
            userName = conf.get("userName");
            password = conf.get("password");
            dbName = conf.get("dbName");
            connectionsNumbersPerHost = Integer.parseInt(conf.get("connectionsNumbersPerHost"));
            threadsAllowedToBlockForConnectionMultiplier = Integer.parseInt(conf.get("threadsAllowedToBlockForConnectionMultiplier"));
            maxWaitTime = Integer.parseInt(conf.get("maxWaitTime"));
            connectTimeout = Integer.parseInt(conf.get("connectTimeout"));
            socketTimeout = Integer.parseInt(conf.get("socketTimeout"));
        } catch (ConfigurationException e) {
            logger.error("The MongoPool initialization parameter is not set correctly");
            e.printStackTrace();
        }


    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
    public static Iterator<String> ListDatabase() {

        if (null == mongoClient) {
            mongoClient = new MongoClient(getServerAddress(), setCredential(userName, dbName, password), getOptions());
        }
        return mongoClient.listDatabaseNames().iterator();
    }

    public static MongoDatabase getMongoDB() {

        if (null == mongoClient) {
            mongoClient = new MongoClient(getServerAddress(), setCredential(userName, dbName, password), getOptions());
        }
        return mongoClient.getDatabase(dbName);
    }

    public static MongoDatabase getMongoDB(String dbName) {

        if (null == mongoClient) {
            mongoClient = new MongoClient(getServerAddress(), setCredential(userName, dbName, password), getOptions());
        }

        return mongoClient.getDatabase(dbName);
    }

    public static MongoDatabase getMongoDB(String dbName, String userName, String password) {

        if (null == mongoClient) {
            mongoClient = new MongoClient(getServerAddress(), setCredential(userName, dbName, password), getOptions());
        }

        return mongoClient.getDatabase(dbName);
    }

    private static MongoCredential setCredential(String userName, String database, String password) {

        MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());


        return credential;
    }

    private static MongoClientOptions getOptions() {
        MongoClientOptions.Builder builder = new MongoClientOptions.Builder();

        if (null != connectionsNumbersPerHost) {
            builder.connectionsPerHost(connectionsNumbersPerHost);
        }
        if (null != threadsAllowedToBlockForConnectionMultiplier) {
            builder.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
        }
        if (null != maxWaitTime) {
            builder.maxWaitTime(maxWaitTime);
        }
        if (null != connectTimeout) {
            builder.connectTimeout(connectTimeout);
        }
        if (null != socketTimeout) {
            builder.socketTimeout(socketTimeout);
        }

        return builder.build();


    }

    private static List<ServerAddress> getServerAddress() {
        List<ServerAddress> serverAddressList = new ArrayList<ServerAddress>(5);
        String[] hosts = host.split(",");

        if (null != hosts && hosts[0] != null) {
            for (int i = 0; i < hosts.length; i++) {
                ServerAddress serverAddress = new ServerAddress(hosts[i], port);
                serverAddressList.add(serverAddress);
            }
        }

        return serverAddressList;

    }

    public static void setHost(String host) {
        MongoPool.host = host;
    }

    public static void setPort(Integer port) {
        MongoPool.port = port;
    }

    public static void setUserName(String userName) {
        MongoPool.userName = userName;
    }

    public static void setPassword(String password) {
        MongoPool.password = password;
    }

    public static void setDbName(String dbName) {
        MongoPool.dbName = dbName;
    }

    public static void setConnectionsNumbersPerHost(Integer connectionsNumbersPerHost) {
        MongoPool.connectionsNumbersPerHost = connectionsNumbersPerHost;
    }

    public static void setThreadsAllowedToBlockForConnectionMultiplier(Integer threadsAllowedToBlockForConnectionMultiplier) {
        MongoPool.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
    }

    public static void setMaxWaitTime(Integer maxWaitTime) {
        MongoPool.maxWaitTime = maxWaitTime;
    }

    public static void setConnectTimeout(Integer connectTimeout) {
        MongoPool.connectTimeout = connectTimeout;
    }

    public static void setSocketTimeout(Integer socketTimeout) {
        MongoPool.socketTimeout = socketTimeout;
    }


    public static void setConf(Map<String, String> conf) {
        MongoPool.conf = conf;
    }

    public static void setMongoClient(MongoClient mongoClient) {
        MongoPool.mongoClient = mongoClient;
    }
}
