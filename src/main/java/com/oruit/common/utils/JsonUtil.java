package com.oruit.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	private JsonUtil(){}

	public static <T> T parseObject(String json,Class<T> clazz){
		return JSON.parseObject(json, clazz);
	}

	public static String toJSONString(Object object){
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
	 * 调用接口失败，返回的数据格式
	 *
	 * @param msg
	 * @param totle
	 * @param data
	 * @return
	 */
	public static JSONObject getErrJSONObject(String msg, String totle, Object data) {
		JSONObject jSONObject = new JSONObject();
		jSONObject.put("totle", totle);
		jSONObject.put("data", data);
		jSONObject.put("msg", msg);
		jSONObject.put("code", "2000");
		return jSONObject;
	}
}
