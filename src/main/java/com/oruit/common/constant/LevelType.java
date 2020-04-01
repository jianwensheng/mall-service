package com.oruit.common.constant;

public enum LevelType {

	USER_V1(1, "初级达人"),

	USER_V2(2, "中级达人"),

	USER_V3(3, "高级达人");

	int code;
	String description;

	LevelType(int code, String description) {
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
