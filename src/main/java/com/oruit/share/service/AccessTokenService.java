 package com.oruit.share.service;

 import com.oruit.share.domain.AccessToken;
 import java.util.List;
 import java.util.Map;
 import org.springframework.stereotype.Service;
 
 @Service
 public interface AccessTokenService {

 
    int update(Map<String, Object> map);
 
    List<AccessToken> listById(AccessToken accessToken);

    AccessToken getToken(String appid, String appsecret);

 }