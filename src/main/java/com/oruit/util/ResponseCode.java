package com.oruit.util;

import java.util.HashMap;
import java.util.Map;

public enum ResponseCode {

    /**
     / * 结果集字段名："code"，String
     */
    CODE("code"),

    /**
     * 结果集字段名："msg",String
     */
    MSG("msg"),

    /**
     * 结果集字段名："datas",String
     */
    DATAS("datas"),

    /**
     * 结果集字段名："detail",String
     */
    DETAIL("detail"),

    /**
     * 成功,code:0,msg:操作成功
     */
    SUCCESS_0(0, "操作成功"),
    /**
     * 参数错误，code：100，msg：参数错误
     */
    PARAM_WRONG_100(100, "参数错误"),
    /**
     * 参数为空,code:101
     */
    CODE_101_PARAM_NULL(101, "参数为空"),
    /**
     * 用户权限不足，code:900
     */
    CODE_900_NO_AUTHORITY(900, "您的账号已在别处登录"),

    /**
     * 暂停注册，code：401
     */
    CODE_401_REGISTER_STOP(401, "暂停注册");

    private static Map<Integer, ResponseCode> codeMap = new HashMap<Integer, ResponseCode>();

    private Integer code;

    private String msg;

    private String value;

    private String customMsg;

    private boolean isCustomMsg = false;

    private ResponseCode() {
    }

    private ResponseCode(String value) {
        this.value = value;
    }

    public String getCustomMsg() {
        return customMsg;
    }

    private ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    public String msg(ResponseLanguage responseLanguage) {
        if (isCustomMsg) {
            this.isCustomMsg = false;
            return this.customMsg;
        }
        return this.msg;
    }

    public String value() {
        return this.value;
    }

    public static ResponseCode getInstance(Integer codeValue) {
        return getCodeMap().get(codeValue);
    }

    private static Map<Integer, ResponseCode> getCodeMap() {
        if (codeMap == null || codeMap.size() == 0) {
            ResponseCode[] codeList = ResponseCode.values();
            for (ResponseCode c : codeList) {
                codeMap.put(c.code(), c);
            }
        }
        return codeMap;
    }

    public ResponseCode customMsg(String customMsg) {
        this.customMsg = customMsg;
        this.isCustomMsg = true;
        return this;
    }

    public ResponseCode addMsg(String addMsg) {
        this.customMsg = this.msg + "," + addMsg;
        this.isCustomMsg = true;
        return this;
    }
}
