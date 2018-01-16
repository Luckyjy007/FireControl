package org.chinalbs.mongodb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.bson.Document;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
Create by jiangyun on 2018/1/12
*/
public class MongoBean implements Cloneable   {

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }

    //对于tojson tojsonString需要get set方法或者public属性 二选一 否则空
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    //结果无序
    public JSONObject toJSONObject() {
        return (JSONObject) JSONObject.toJSON(this);
    }

    //结果有序
    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    //依赖get set方法  结果无序
    public Map<String, Object> toMap() {
        if (this == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(this.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                /*过滤class属性  */
                if (!key.equals("class")) {
                    //*得到property对应的getter方法*/
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(this);

                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //不管属性private还是有无get set方法都可以 结果有序
    public Map<String, Object> objectObjectMap() {
        if (this == null) {
            return null;
        }
        HashMap map = new HashMap(40);
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public Document toDocument() {
        return Document.parse(toJSONString());
    }

}
