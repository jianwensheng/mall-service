package com.oruit.common.utils;

import com.alibaba.fastjson.JSONObject;

public class ResultJson {

    private String code;
    private String msg;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static JSONObject result(String code, String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;

    }

    public static JSONObject result(String code, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("data", "");
        return jsonObject;

    }

    public static JSONObject resultOk() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "200");
        return jsonObject;

    }

    public static JSONObject resultOk(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "200");
        jsonObject.put("msg", msg);
        jsonObject.put("data", "");
        return jsonObject;

    }

    public static JSONObject resultOk(String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "1000");
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;

    }

    public static JSONObject resultError(String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "2000");
        jsonObject.put("msg", msg);
        jsonObject.put("data", "");
        return jsonObject;

    }
    public static JSONObject resultError() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "2000");
        jsonObject.put("msg", "");
        jsonObject.put("data", "");
        return jsonObject;

    }

    public static JSONObject resultError(String msg, Object data) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", "2000");
        jsonObject.put("msg", msg);
        jsonObject.put("data", data);
        return jsonObject;

    }
}
