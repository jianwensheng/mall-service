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

    @JSONField(name = "openid")
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

    private String code;

    @JSONField(name = "access_token")
    private String token;

    private String language;

    private static final long serialVersionUID = 1L;


}