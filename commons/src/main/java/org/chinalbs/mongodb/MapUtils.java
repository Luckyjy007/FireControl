package org.chinalbs.mongodb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/*
Create by jiangyun on 2018/1/17
*/
public class MapUtils {
    private static final Logger logger = LoggerFactory.getLogger(MapUtils.class);

    public static void main(String[] args) {
        Map map = new HashMap(10);
        map.put("s", "s");
        map.put("1", "2");
        map.put("2", "2");
        map.put("1", "22");
        System.out.println(mapToJson(map));
        System.out.println(mapToString(map));
        System.out.println(toJsonobj(map));

    }


    public static String mapToString(Map<String, String> map) {
        if (null == map || map.size() < 1) {
            return null;
        }
        String result = "";
        for (Map.Entry<String, String> entry : map.entrySet()) {

            result += entry.getKey() + ":" + entry.getValue() + ",";
        }
        return result;
    }

    public static String mapToJson(Map map) {

        return JSON.toJSONString(map);
    }

    public static JSON toJsonobj(Map map) {
        return (JSONObject) JSONObject.toJSON(map);
    }

}
