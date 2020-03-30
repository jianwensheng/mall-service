package com.oruit.share.model;

import com.alibaba.fastjson.JSONObject;
import com.oruit.util.ResponseCode;
import com.oruit.util.ResponseLanguage;

import java.io.Serializable;

/**
 * @Description 返回结果集
 * @author Huangjunfeng
 * @date 2017年3月21日
 */
public class BaseResult<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer code;

	private String msg;
	
	private long serverTime = System.currentTimeMillis();

	private T datas;

	public ResponseCode responseCode() {
		return ResponseCode.getInstance(this.code);
	}

	public void setResponseCode(ResponseCode responseCode) {
		if (responseCode != null) {
			this.code = responseCode.code();
			this.msg = responseCode.msg(ResponseLanguage.CHINESE);
		}
	}

	public T getDatas() {
		return datas;
	}

	public void setDatas(T datas) {
		this.datas = datas;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static <T> BaseResult<T> success() {
		return getInstance(ResponseCode.SUCCESS_0, null);
	}

	public static <T> BaseResult<T> success(T datas) {
		return getInstance(ResponseCode.SUCCESS_0, datas);
	}

	public static <T> BaseResult<T> getInstance(ResponseCode responseCode) {
		return getInstance(responseCode, null);
	}

	public static <T> BaseResult<T> getInstance(ResponseCode responseCode, T datas) {
		if (responseCode == null) {
			return null;
		}
		BaseResult<T> baseResult = new BaseResult<T>();
		baseResult.setCode(responseCode.code());
		baseResult.setMsg(responseCode.msg());
		if (datas != null) {
			baseResult.setDatas(datas);
		}
		return baseResult;
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public static void main(String[] args) {

	}

	public long getServerTime() {
		return System.currentTimeMillis();
	}

	public void setServerTime(long serverTime) {
		this.serverTime = System.currentTimeMillis();
	}
}
