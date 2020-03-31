package com.oruit.util;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class StandardResult implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private JSONObject datas;

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public JSONObject getDatas() {
        return datas;
    }

    public void setDatas(JSONObject datas) {
        this.datas = datas;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public ResponseCode responseCode() {
        if (code == null) {
            return null;
        }
        return ResponseCode.getInstance(code);
    }
    
    public static StandardResult getInstance(ResponseCode responseCode) {
    	return getInstance(responseCode, null);
    }
    
    public static StandardResult getInstance(ResponseCode responseCode, Object datas) {
    	if(responseCode == null) {
    		return null;
    	}
    	StandardResult standardResult = new StandardResult();
    	standardResult.setCode(responseCode.code());
    	standardResult.setMsg(responseCode.msg(ResponseLanguage.CHINESE));
    	if(datas != null) {
    		standardResult.setDatas(JSONObject.parseObject(JSONObject.toJSONString(datas)));
    	}
    	return standardResult;
    }

    public static void main(String[] args) {

    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
