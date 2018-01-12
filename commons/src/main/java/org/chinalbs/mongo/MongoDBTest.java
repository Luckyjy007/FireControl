package org.chinalbs.mongo;


import com.alibaba.fastjson.JSON;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MongoDBTest {

    private static final String HOST = "cors-01";
    private static final int PORT = 27017;
    private static final String DB_NAME = "testDB";
    private static MongoClient mongoClient;
    private static MongoDatabase db;

    static {
        // 连接到MongoDB
        mongoClient = new MongoClient(HOST, PORT);


        // 打开数据库 testDB
        db = mongoClient.getDatabase(DB_NAME);
    }

    public static void main(String[] args) {
        // 获取集合 xbqTable，若该集合不存在，mongoDB将自动创建该集合
        MongoCollection<Document> dbCollection = db.getCollection("testTable");

        // 查询该数据库所有的集合名
        for (String name : mongoClient.listDatabaseNames()) {
            System.out.println(name);
        }
        queryList(dbCollection);

    }


    // ====================================查询开始==============================================

    /**
     * @param dbCollection
     * @Title: queryOne
     * @Description: TODO 查询 name为 张三的 一条记录
     * @return: void
     */
    public static void queryAll(MongoCollection<Document> dbCollection) {
        FindIterable<Document> result = dbCollection.find();
        MongoCursor<Document> iterator = result.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            System.out.println(document.toString());
        }
    }

    /**
     * @param dbCollection
     * @Title: queryPage
     * @Description: TODO 分页查询  ， 查询 跳过前2条 后的 3条 数据
     * @return: void
     */
    public static void queryPage(DBCollection dbCollection) {
        DBCursor cursor = dbCollection.find().skip(2).limit(3);
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    /**
     * @param dbCollection
     * @Title: queryRange
     * @Description: TODO 范围查询，查询 第3条 到 第5条 之间的记录
     * @return: void
     */
    public static void queryRange(DBCollection dbCollection) {
        DBObject range = new BasicDBObject();
        range.put("$gte", 50);
        range.put("$lte", 52);

        DBObject dbObject = new BasicDBObject();
        dbObject.put("age", range);
        DBCursor cursor = dbCollection.find(dbObject);
        while (cursor.hasNext()) {
            System.out.println(cursor.next());
        }
    }

    /**
     * '
     *
     * @param dbCollection
     * @Title: queryList
     * @Description: TODO 查询出全部的 记录
     * @return: void
     */
    public static void queryList(MongoCollection<Document> dbCollection) {
        FindIterable<Document> cursor = dbCollection.find();
        DBObject dbObject = null;
        MongoCursor<Document> documentMongoCursor = cursor.iterator();
        while (documentMongoCursor.hasNext()) {
            Document document = documentMongoCursor.next();
            System.out.println(document);
        }
    }

    // ====================================增加开始==============================================

    /**
     * @param dbCollection
     * @Title: addOne
     * @Description: TODO 新增 一条记录
     * @return: void
     */
    public static void addOne(DBCollection dbCollection) {
        DBObject documents = new BasicDBObject("name", "张三").append("age", 45).append("sex", "男").append("address",
                new BasicDBObject("postCode", 100000).append("street", "深南大道888号").append("city", "深圳"));
        dbCollection.insert(documents);
    }

    /**
     * @param dbCollection
     * @Title: addList
     * @Description: TODO 批量新增 记录    , 增加的记录 中 可以使用各种数据类型
     * @return: void
     */
    public static void addList(DBCollection dbCollection) {
        List<DBObject> listdbo = new ArrayList<DBObject>();
        DBObject dbObject = new BasicDBObject();
        dbObject.put("name", "老王");
        // 可以直接保存List类型
        List<String> list = new ArrayList<String>();
        list.add("非隔壁老王");
        dbObject.put("remark", list);
        listdbo.add(dbObject);

        dbObject = new BasicDBObject();
        // 可以直接保存map
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> hobbys = new ArrayList<String>();
        hobbys.add("看花");
        hobbys.add("采花");
        map.put("爱好", hobbys);
        dbObject.put("hobby", map);
        listdbo.add(dbObject);

        dbObject = new BasicDBObject();
        dbObject.put("name", "老张");
        dbObject.put("age", 52);
        dbObject.put("job", "看守老王");
        dbObject.put("remark", new BasicDBObject("address", "广东省深圳市").append("street", "深南大道888号"));
        listdbo.add(dbObject);

        dbCollection.insert(listdbo);
    }

    /**
     * @param dbCollection
     * @Title: addByJson
     * @Description: TODO json转对象后 ，执行新增
     * @return: void
     */
    public static void addByJson(DBCollection dbCollection) {
        String json = "{ \"name\" : \"王五\" , \"age\" : 66 , \"job\" : \"看守老王\" , \"remark\" : { \"address\" : \"广东省深圳市\" , \"street\" : \"深南大道888号\"}}";
        DBObject dbObject = (DBObject) JSON.parse(json);
        dbCollection.insert(dbObject);
    }

    // ====================================修改开始==============================================

    /**
     * @param dbCollection
     * @Title: update
     * @Description: TODO 修改指定记录
     * @return: void
     */
    public static void updateOne(DBCollection dbCollection) {
        // 先根据id查询将 这条 记录查询出来
        DBObject qryResult = dbCollection.findOne(new ObjectId("58e4a11c6c166304f0635958"));
        // 修改指定的值
        qryResult.put("age", 55);

        DBObject olddbObject = new BasicDBObject();
        olddbObject.put("_id", new ObjectId("58e4a11c6c166304f0635958"));
        dbCollection.update(olddbObject, qryResult);
    }

    /**
     * @param dbCollection
     * @Title: updateMulti
     * @Description: TODO 修改 多条记录
     * @return: void
     */
    public static void updateMulti(DBCollection dbCollection) {
        DBObject newdbObject = new BasicDBObject();
        newdbObject.put("name", "张三");
        newdbObject.put("address", "广东深圳");
        newdbObject.put("remark", "张三是一个NB的Coder");

        DBObject olddbObject = new BasicDBObject();
        olddbObject.put("name", "张三");
        // 需要加上这个
        DBObject upsertValue = new BasicDBObject("$set", newdbObject);
        // 后面的两个参数：1.若所更新的数据没有，则插入 ; 2、同时更新多个符合条件的文档(collection)
        dbCollection.update(olddbObject, upsertValue, true, true);
    }

    // ====================================删除开始==============================================

    /**
     * @param
     * @Title: deleteFirst
     * @Description: TODO 删除第一个
     * @return: void
     */
    public static void deleteFirst(DBCollection dbCollection) {
        DBObject dbObject = dbCollection.findOne();
        dbCollection.remove(dbObject);
    }

    /**
     * @param dbCollection
     * @Title: deleteOne
     * @Description: TODO 删除指定的一条记录
     * @return: void
     */
    public static void deleteOne(DBCollection dbCollection) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("_id", new ObjectId("58e49c2d6c166309e0d50484"));
        dbCollection.remove(dbObject);
    }

    /**
     * @param dbCollection
     * @Title: deleteByIn
     * @Description: TODO 删除多条记录      例如：select * from tb where name in('12','34')
     * @return: void
     */
    public static void deleteByIn(DBCollection dbCollection) {
        List<String> list = new ArrayList<String>();
        list.add("老张");
        list.add("老王");
        list.add("张三");
        DBObject dbObject = new BasicDBObject("$in", list);

        DBObject delObject = new BasicDBObject();
        delObject.put("name", dbObject);
        dbCollection.remove(delObject);
    }

    /**
     * @param dbCollection
     * @Title: deleteAll
     * @Description: TODO 删除全部的记录
     * @return: void
     */
    public static void deleteAll(DBCollection dbCollection) {

        DBCursor cursor = dbCollection.find();
        while (cursor.hasNext()) {
            dbCollection.remove(cursor.next());
        }
    }

}
