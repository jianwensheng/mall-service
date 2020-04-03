package com.oruit.share.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TbUser implements Serializable {
    private Long id;

    @JSONField(name = "nickname")
    private String username;

    private String mobile;

    private String email;

    private Short sex;

    private String country;

    private String province;

    private String city;

    private String openId;

    private Short userType;

    private Short status;

    private Date createTime;

    private Date updateTime;

    private String password;

    @JSONField(name = "unionid")
    private String unionId;

    @JSONField(name = "headimgurl")
    private String headPic;

    private String deviceid;

    private String wechat;

    //邀请码
    private String code;



    @JSONField(name = "access_token")
    private String token;

    private String language;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "TbUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", mobile='" + mobile + '\'' +
                ", email='" + email + '\'' +
                ", sex=" + sex +
                ", country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", openId='" + openId + '\'' +
                ", userType=" + userType +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", password='" + password + '\'' +
                ", unionId='" + unionId + '\'' +
                ", headPic='" + headPic + '\'' +
                ", deviceid='" + deviceid + '\'' +
                ", wechat='" + wechat + '\'' +
                ", code='" + code + '\'' +
                ", token='" + token + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}