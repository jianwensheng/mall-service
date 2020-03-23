package com.oruit.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.cache.redis.RedisUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class TestUtil {

    public static String client_id = "";

    public static String result_url = "";




    public static void main(String[] args) {
     /*   String keyWords = "意大利KIKO4系口红女唇膏豆沙姨妈色学生热卖 【包邮】";
        TreeMap<String,String> paraMap = new TreeMap<>();
        paraMap.put("version","v2.0.0");
        paraMap.put("pageNo","1");
        paraMap.put("pageSize","20");
        paraMap.put("keyWords",keyWords);
        paraMap.put("source","1");
        paraMap.put("hasCoupon","true");
        paraMap.put("sort","total_sales_des");
        String str = HttpUtils.sendGet(MethodUtil.tb_good_url,MethodUtil.postContent(paraMap));
        System.out.println(str);*/

        BigDecimal actualPrice = new BigDecimal(59.90);
        BigDecimal commissionRate = new BigDecimal(1.8);
        BigDecimal commission =  actualPrice.multiply(commissionRate).divide(new BigDecimal(100));
        Double num = commission.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(num);

    }
}
