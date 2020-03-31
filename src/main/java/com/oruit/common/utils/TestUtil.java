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

    static String appId = "wx3f89d2c7ededa0d9";

    static String appSecret = "417fb5ac86136846e07d7697fa0aba09";

    static String code = "011m4jfH1iGAb30KwKhH1A6TeH1m4jf0";

    public static void main(String[] args) {
        String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
        System.out.println(token_url);
        String result = HttpUtils.doGet(token_url);
        System.out.println(result);

    }
}
