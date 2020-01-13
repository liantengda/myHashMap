package com.lian.hashmapdemo.test;

import com.lian.hashmapdemo.hashmapinterface.MyMap;
import com.lian.hashmapdemo.model.MyHashMap;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        MyMap<String, String> myHashMap = new MyHashMap<>();
        for(int i=0;i<10000;i++){
            myHashMap.put("hello"+i,"world"+i);
        }
        System.out.println("哈希碰撞次数--->"+MyHashMap.conflictCount);
        System.out.println(myHashMap.get("hello"));
    }
}
