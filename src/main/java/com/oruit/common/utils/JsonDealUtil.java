package com.oruit.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonDealUtil {

    private JsonDealUtil() {
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        return JSON.parseObject(json, clazz);
    }

    public static String toJSONString(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 调用接口成功，返回的数据格式
     *
     * @param msg
     * @param totle
     * @param data
     * @return
     */
    public static JSONObject getSuccJSONObject(String msg, String totle, Object data) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("totle", totle);
        jSONObject.put("data", data);
        jSONObject.put("msg", msg);
        jSONObject.put("code", "1000");
        return jSONObject;
    }


    /**
     * 调用接口成功，返回的数据格式 增加返回弹窗时间
     *
     */
    public static JSONObject getSuccToastJSONObject(String msg, String totle, Object data, String toastMs) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("totle", totle);
        jSONObject.put("data", data);
        jSONObject.put("msg", msg);
        jSONObject.put("toastMs", toastMs);
        jSONObject.put("code", "1000");
        return jSONObject;
    }

    public static JSONObject getSuccQiandao() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("msg", "0|已经签到");
        jSONObject.put("code", "2000");
        return jSONObject;
    }

    public static JSONObject getSuccVcode() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("msg", "0|手机号重复");
        jSONObject.put("code", "2001");
        return jSONObject;
    }

    /**
     * 调用接口失败
     *
     * @param msg
     * @return
     */
    public static JSONObject getErrJSONObject(String msg) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("msg", msg);
        jSONObject.put("data", "");
        jSONObject.put("code", "2000");
        jSONObject.put("totle", "1");
        return jSONObject;
    }

    /**
     * 没有数据
     *
     * @return
     */
    public static JSONObject getNullJSONObject() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("msg", "1|no data");
        jSONObject.put("code", "2100");
        jSONObject.put("data", "");
        jSONObject.put("total", "1");
        return jSONObject;
    }

    /**
     * 自定义返回Code
     * code值写公用类JSONObjectCode中   xuzhen
     * @param msg
     * @return
     */
    public static JSONObject getCodeJSONObject(String msg, String code) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("msg", msg);
        jSONObject.put("data", "");
        jSONObject.put("code", code);
        jSONObject.put("totle", "1");
        return jSONObject;
    }


    /**
     * 根据key取出json字符串中的值
     */
    public static String getValueBykeyFromJson(String jsonStr,String key){
    	JSONObject jsonObject = JSONObject.parseObject(jsonStr);
    	return jsonObject.getString(key);
    }
}
