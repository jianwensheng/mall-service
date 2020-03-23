package com.oruit.common.constant;

/**
 * @author: wangyt
 * @date: 2019-08-27 15:32
 * @description: TODO
 */
public enum ConstUtils {


    NO_AD_LEVEL("-10", "广告分享受限用户"),
    IS_DEAL_AD_FOR_SHOW_RESTRICTED_USER("1", "是否对受限用户显示广告进行处理： 0 ： 不处理 1：处理"),
    REAL_URL_FLAG("466720acd1e742f18de7f7d19a1ec", ""),
    SHOW_URL_FLAG("46672oacd1e742f18de7f7d19a1ec", ""),
    TABLE_SCHEMA("intl_guagua", "数据库名称"),
    NUll("null", "是否为 null 字符串"),
    SLASH("/", "/"),
    VERTICAL("|", "|"),
    DEAL("/deal", ""),
    ADS("/ads", ""),
    HTTP("http://", ""),
    ERROR("/error", ""),
    CLICK("click", "点击展开全文"),
    NOTCLICK("notclick", "未点击展开全文"),
    AD("10", ""),
    ONE("1", ""),
    NORMAL("20", ""),
    TYPE("type", ""),
    USERID("userid", ""),
    COUNTRY("country", "国家"),
    LANCODE("lancode", "国家编码"),
    DOMAINNAME("domainname", ""),
    DIAN(".", "");


    private String value;
    private String desc;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    ConstUtils(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}
