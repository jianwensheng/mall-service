package com.oruit.share.controller;

import com.oruit.common.utils.StringUtils;
import com.oruit.share.domain.AccessToken;
import com.oruit.share.domain.TbUser;
import com.oruit.share.model.BaseResult;
import com.oruit.share.service.AccessTokenService;
import com.oruit.share.service.UserService;
import com.oruit.util.ResponseCode;
import com.oruit.weixin.WxUtils;
import com.oruit.weixin.util.WXUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class LoginController {

    @Value("${weixin.url}")
    private String NET_WEB;

    @Value("${login.wx.appId}")
    private String appId;

    @Value("${login.wx.appSecret}")
    private String appSecret;

    @Autowired
    private UserService userService;

    @Autowired
    private AccessTokenService accessTokenService;


    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
        String url = null;
        if (session.getAttribute("open_id") == null) {
            String inviteUrl = NET_WEB +"/login";
            inviteUrl = URLEncoder.encode(inviteUrl, "utf-8");
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + inviteUrl
                    + "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
            response.sendRedirect(url);
        }else{
            url= NET_WEB+"/index";
            response.sendRedirect(url);
        }
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request, Model model, HttpSession session,
                      String code,HttpServletResponse response)throws IOException {
        System.out.println("code:" + code);
        if (StringUtils.isEmpty(code)) {
            return;
        }
        try {
            AccessToken accessToken = WXUtil.getAccessToken(code,appId,appSecret);
            if (accessToken != null) {
                TbUser userInfo = WXUtil.getUserInfo(accessToken.getAccessToken(), accessToken.getOpenId());
                if (userInfo != null) {
                    if(userInfo.getOpenId()==null) {
                        log.info("openId is null,CODE_401_REGISTER_STOP");
                        return;
                    }
                    TbUser login = userService.generateTokenAndSave(userInfo);
                    if (login == null) {
                        log.info("login is null,CODE_401_REGISTER_STOP");
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String homeUrl = NET_WEB +"/index";
        response.sendRedirect(homeUrl);
    }
}
