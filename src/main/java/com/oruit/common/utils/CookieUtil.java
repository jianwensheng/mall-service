package com.oruit.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by Administrator on 2019/8/28.
 */
public class CookieUtil {


    //生成openid,相当于设备id,用于记量作弊限制
    public static String getOpenIdCookie(HttpServletRequest request, HttpServletResponse response) {
        String openid = "";
        //取cookie里已经生成的openid
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equals("user_identification")) {
                    openid = c.getValue();
                }
            }
        }

        //不存在openid，生成
        if (openid == null || "".equals(openid) || "null".equals(openid)) {
            openid = "flg" + UUID.randomUUID().toString();
            Cookie cookie = new Cookie("user_identification", openid);
            cookie.setMaxAge(999999999);
            response.addCookie(cookie);
        }
        return openid;

    }

}
