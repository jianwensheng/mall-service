package com.oruit.share.controller;

import com.oruit.common.utils.StringUtils;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.UserService;
import com.oruit.weixin.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
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

    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;

    @Value("${weixin.url}")
    private String NET_WEB;

    @Autowired
    private UserService userService;


    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
        //session.setAttribute("openId","ouA-Q1sPSt6s0Lo8_B9svq7Xbii8");
        String openId = session.getAttribute("openId") != null?session.getAttribute("openId").toString():"";
        log.info("首页 openId：{}",openId);
        String url;
        if (StringUtils.isEmpty(openId)) {
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
    public void login(HttpSession session,String code,HttpServletResponse response)throws IOException {
        String openId = session.getAttribute("openId") != null?session.getAttribute("openId").toString():"";
        log.info("系统开始，检查openId={}",openId);
        if (StringUtils.isNotEmpty(openId)) {
            TbUser user = WxUtils.openIdInfo(code,appId,appSecret,session);
            userService.toUserInserOrUpdate(user);
        }else {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.info("open_id==null");
        }
        String homeUrl = NET_WEB +"/index";
        response.sendRedirect(homeUrl);
    }
}
