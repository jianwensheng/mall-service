 package com.oruit.share.domain;

 import lombok.Data;

 @Data
 public class AccessToken {

     Integer id;

     String accessToken;

     Long addTime;

     Integer expiresIn;

 }