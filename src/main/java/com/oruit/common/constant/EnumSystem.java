package com.oruit.common.constant;

public enum EnumSystem {

    ANDROID(1, "android"), IOS(2, "iOS"), ANDROID_IOS(3, "android_iOS");
    int code;
    String description;

    EnumSystem(int code, String description) {
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
