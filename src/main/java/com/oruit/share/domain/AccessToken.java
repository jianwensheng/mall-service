 package com.oruit.share.domain;

 import com.alibaba.fastjson.annotation.JSONField;
 import lombok.Data;

 @Data
 public class AccessToken {

     Integer id;

     @JSONField(name = "access_token")
     String accessToken;

     Long addTime;

     @JSONField(name = "expires_in")
     Integer expiresIn;

     @JSONField(name = "openid")
     private String openId;

     @JSONField(name = "unionid")
     private String unionId;

 }