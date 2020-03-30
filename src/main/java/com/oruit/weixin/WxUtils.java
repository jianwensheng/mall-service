package com.oruit.weixin;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oruit.common.utils.HttpUtils;
import com.oruit.common.utils.JsonDealUtil;
import com.oruit.common.utils.MethodUtil;
import com.oruit.common.utils.StringUtils;
import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.share.domain.AccessToken;
import com.oruit.share.domain.TbUser;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class WxUtils {


    public static TbUser oppenIdInfo(String code, String appId, String appSecret, AccessToken accessToken,HttpSession session) {
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
                user.setToken(accessToken.getAccessToken());
                session.setAttribute("open_id", user.getOpenId());
                weixinUserInfo(user,session);
                return user;
            } catch (Exception e) {
                log.error("解析微信返回信息異常", e.getMessage());
            }
        }
        return null;
    }


    public static void weixinUserInfo(TbUser user,HttpSession session) {
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", user.getToken()).replace("OPENID", user.getOpenId());
        log.info("weixinUserInfo,requestUrl={}", requestUrl);
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
                session.setAttribute("user", user);
            } catch (Exception e) {
                log.error("解析微信返回信息異常", e);
            }
    }

    /**
     * 根据淘口令查询商品ID
     * @param tkl
     * @return
     */
    public static String TbGoodInfoByGoodId(String tkl,String apikey){
       String tklKey = MethodUtil.good_tkl_key+tkl;
        String obj = RedisUtil.get(tklKey);
        if(obj!=null){
            return obj;
        }
       String requestUrl = "https://api.taokouling.com/tkl/tkljm?apikey=API_KEY&tkl=￥TKL￥";
       requestUrl = requestUrl.replace("API_KEY", apikey).replace("TKL", tkl);
       String result = HttpUtils.doGet(requestUrl);
       JSONObject jsonObject = JSONObject.parseObject(result,JSONObject.class);
        try {
            String code = jsonObject.get("code").toString();
            if(code.equals("1")){
                //调用成功,获取商品ID
                String url = jsonObject.get("url").toString();
                String goodsId = url.split("\\?")[0].split("/")[3].split("\\.")[0].substring(1);
                RedisUtil.setByTime(tklKey, goodsId,60*10);
                return goodsId;
            }
        } catch (Exception e) {
            log.error("淘口令解析失败:{}",e.getMessage());
        }
        return null;
    }




}
