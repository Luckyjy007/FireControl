package org.chinalbs.mongodb;

import java.util.HashMap;

/*
Create by jiangyun on 2018/1/15
*/
public class Bean extends MongoBean {
    private String _id;
    private int age;
    private double salary;
    private String name;
    private HashMap<String, Object> hashMap;

    public Bean() {
    }

    public Bean(String _id, int age, double salary, String name, HashMap<String, Object> hashMap) {
        this._id = _id;
        this.age = age;
        this.salary = salary;
        this.name = name;
        this.hashMap = hashMap;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, Object> hashMap) {
        this.hashMap = hashMap;
    }

    public String getUserId() {
        return _id;
    }

    public void setUserId(String userId) {
        this._id = _id;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "userId=" + _id +
                ", age=" + age +
                ", salary=" + salary +
                ", name='" + name + '\'' +
                ", hashMap=" + hashMap +
                '}';
    }


}
