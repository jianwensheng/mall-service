package com.oruit.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.cache.redis.RedisUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUtil {

    public static void main(String[] args) {
        String str = "https://a.m.taobao.com/i605657989518.htm?price=69-135&sourceType=item&sourceType=item&suid=" +
                "20bd2342-a3b4-4b86-89c2-ea03bf408965&ut_sk=1.XCv%2FBoDSlz8DANbbPLb7CTQ1_21646297_1585321159486.Copy.1&un=" +
                "e2f765742f6bf4fc8a195baaa22d8f6a&share_crt_v=1&spm=a2159r.13376460.0.0&sp_tk=4oK0bkMxdTFSamZna0zigrQ=";
        String goodsId = str.split("\\?")[0].split("/")[3].split("\\.")[0].substring(1);
        System.out.println(goodsId);
    }
}
