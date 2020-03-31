package com.oruit.share.service.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.share.dao.AccessTokenMapper;
import com.oruit.share.domain.AccessToken;
import com.oruit.share.service.AccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    @Autowired
    public AccessTokenMapper accessTokenMapper;

    public int update(Map<String, Object> map)
    {
        return accessTokenMapper.update(map);
    }

    public List<AccessToken> listById(AccessToken accessToken)
    {
        return accessTokenMapper.listById();
    }

    @Override
    public AccessToken getToken(String appid, String appsecret) {
        Long nowtime = Long.valueOf(new Date().getTime());
        List token_list = accessTokenMapper.listById();
        System.out.println(token_list.size() + ((AccessToken)token_list.get(0)).getAccessToken());
        Long add_time = ((AccessToken)token_list.get(0)).getAddTime();
        AccessToken token = new AccessToken();
        if (add_time.longValue() + 7200000L <= nowtime.longValue()) {
            String requestUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET".replace("APPID", appid).replace("APPSECRET", appsecret);

            String requestJson = HttpUtils.doGet(requestUrl);
            JSONObject jsonObject = JSONObject.parseObject(requestJson,JSONObject.class);
            try{
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
                Map map = new HashMap();
                map.put("addTime", nowtime);
                map.put("accessToken", jsonObject.getString("access_token"));
                map.put("expiresIn", token.getExpiresIn());
                accessTokenMapper.update(map);
            }catch (JSONException e) {
                token = null;
                log.error("获取token失败 errcode:{} errmsg:{}", Integer.valueOf(jsonObject.getInteger("errcode")), jsonObject.getString("errmsg"));
                System.out.println("-----------获取token失败");
            }
        }
        else {
            token.setExpiresIn(7200);
            token.setAccessToken(((AccessToken)token_list.get(0)).getAccessToken());
        }
        return token;
    }
}
