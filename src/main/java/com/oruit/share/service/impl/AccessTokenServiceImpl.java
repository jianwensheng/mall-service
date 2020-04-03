package com.oruit.share.service.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.MethodUtil;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.share.dao.AccessTokenMapper;
import com.oruit.share.domain.AccessToken;
import com.oruit.share.service.AccessTokenService;
import com.oruit.weixin.CommonUtil;
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

    @Override
    public String createPermanentQRCode(String accessToken, int sceneId) {
        String key = MethodUtil.qrcode_url_key+sceneId;
        Object obj = RedisUtil.get(key);
        if(obj!=null){
            return obj.toString();
        }
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken);
        String jsonMsg = "{\"expire_seconds\": 604800,\"action_name\": \"QR_LIMIT_SCENE\", \"action_info\": {\"scene\": {\"scene_id\": %d}}}";
        net.sf.json.JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "POST", String.format(jsonMsg, new Object[] { Integer.valueOf(sceneId) }));
        String imgPath = null;
        if (jsonObject != null) {
            try {
                imgPath = jsonObject.getString("url");
                log.info("创建永久带参二维码成功 url:{}", imgPath);
                RedisUtil.setByTime(key,imgPath,MethodUtil.week_expires);
            } catch (Exception e) {
                int errorCode = jsonObject.getInt("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("创建永久带参二维码失败 errcode:{} errmsg:{}", Integer.valueOf(errorCode), errorMsg);
            }
        }
        return imgPath;
    }

    @Override
    public String getQRCode(String ticket) {
        String requestUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";
        requestUrl = requestUrl.replace("TICKET", CommonUtil.urlEncodeUTF8(ticket));
        return requestUrl;
    }
}
