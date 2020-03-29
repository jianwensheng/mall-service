package com.oruit.share.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TbUser implements Serializable {
    private Long id;

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

    private String unionId;

    private String headPic;

    private String deviceid;

    private String wechat;

    private String code;

    private String token;

    private String language;

    private static final long serialVersionUID = 1L;


}