package com.oruit.common.utils;

import java.util.TreeMap;

public class Demo {
    public static final String appSecret = "b6adf9856994f2a30df85c3c76fb89ab";//应用sercret
    public static final String appKey = "5d2c1519c5fe9"; //应用key
    private static final String host = "https://openapi.dataoke.com/api/category/ddq-goods-list";//应用服务接口
    public static void main(String[] args) {
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v1.2.0");
        paraMap.put("appKey",appKey);
        paraMap.put("sign", SignMD5Util.getSignStr(paraMap,appSecret));
        System.out.println(HttpUtils.sendGet(host,paraMap));
    }
}
