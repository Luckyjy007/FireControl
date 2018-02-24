package org.chinalbs.mongodb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/*
Create by jiangyun on 2018/1/12
*/
public class MongoUtls {


    private static final Logger logger = LoggerFactory.getLogger(MongoUtls.class);


    public static MongoDatabase getMongoDB() {
        return MongoPool.getMongoDB();
    }

    public static MongoDatabase getMongoDB(String dbName) {
        return MongoPool.getMongoDB(dbName);
    }

    public static MongoDatabase getMongoDB(String userName, String dbName, String password) {
        return MongoPool.getMongoDB(userName, dbName, password);
    }

    public static Iterator<String> ListDatabase() {
        return MongoPool.ListDatabase();
    }

    public static MongoCollection<Document> getCollection(MongoDatabase database, String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return database.getCollection(collectionName);

    }


    private static JSONObject parseObject(String json) {
        try {
            if (json != null && !(json == "")) {
                return JSON.parseObject(json);
            }
            return new JSONObject();
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new JSONObject();
        }


    }

    private static <T> T parseObject(Document doc, Class<T> clazz) {
        try {
            if (doc == null) {
                return JSON.parseObject(new JSONObject().toJSONString(), clazz);
            }
            return JSON.parseObject(JSON.toJSONString(doc), clazz);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return JSON.parseObject(new JSONObject().toJSONString(), clazz);
        }
    }

    private static JSONObject toJSON(Object obj) {
        try {
            return (JSONObject) JSON.toJSON(obj);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }


}
