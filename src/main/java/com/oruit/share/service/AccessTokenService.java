 package com.oruit.share.service;

 import com.oruit.share.domain.AccessToken;
 import java.util.List;
 import java.util.Map;

 import com.oruit.share.domain.TbUser;
 import org.springframework.stereotype.Service;

 import javax.servlet.http.HttpSession;

 @Service
 public interface AccessTokenService {

 
    int update(Map<String, Object> map);
 
    List<AccessToken> listById(AccessToken accessToken);

    AccessToken getToken(String appid, String appsecret);

     /**
      * 创建临时二维码
      * @param accessToken
      * @param sceneId
      * @return
      */
    String createPermanentQRCode(String accessToken, int sceneId);

     /**
      * 生成临时二维码图片
      * @param ticket
      * @return
      */
    String getQRCode(String ticket);
 }