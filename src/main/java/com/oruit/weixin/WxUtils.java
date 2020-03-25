package com.oruit.weixin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.StringUtils;
import com.oruit.share.domain.TbUser;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WxUtils {

    public static TbUser open_id(String code, String appId, String appSecret) {
        String token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";

        String result = HttpUtils.doGet(token_url);
        if (StringUtils.isNotBlank(result)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                log.info("wexin_openId,result={}", result);
                Map<String, String> userMap = (Map<String, String>)mapper.readValue(result, HashMap.class);
                TbUser user = new TbUser();
                user.setOpenId(userMap.get("openid"));
                user.setCode(code);
                user.setAccessToken(userMap.get("access_token"));
                return user;
            } catch (Exception e) {
                log.error("解析微信返回信息異常", e.getMessage());
            }
        }
        return null;
    }


    public static void weixinUserInfo(TbUser user) {
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", user.getAccessToken()).replace("OPENID", user.getOpenId());
        String result = HttpUtils.doGet(requestUrl);
        log.info("weixinUserInfo,result={}", result);
        if (StringUtils.isNotBlank(result))
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> userMap = (Map<String, Object>)mapper.readValue(result, HashMap.class);
                user.setUsername(EmojiFilter.filterEmoji(String.valueOf(userMap.get("nickname"))));
                user.setSex(Short.valueOf(userMap.get("sex").toString()));
                user.setCountry(String.valueOf(userMap.get("country")));
                user.setProvince(String.valueOf(userMap.get("province")));
                user.setCity(String.valueOf(userMap.get("city")));
                user.setLanguage(String.valueOf(userMap.get("language")));
                user.setHeadPic(String.valueOf(userMap.get("headimgurl")));
                if (userMap.get("unionid") != null) {
                    user.setUnionId(String.valueOf(userMap.get("unionid")));
                }
            } catch (Exception e) {
                log.error("解析微信返回信息異常", e);
            }
    }
}
