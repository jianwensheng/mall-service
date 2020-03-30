package com.oruit.common.constant;

public enum  PlatType {

	ANDROID(1, "taobao"), IOS(2, "pdd"), ANDROID_IOS(3, "jd");
	int code;
	String description;

	PlatType(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}
}
