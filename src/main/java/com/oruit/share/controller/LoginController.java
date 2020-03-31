package com.oruit.share.controller;

import com.oruit.common.utils.StringUtils;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.UserService;
import com.oruit.weixin.WxUtils;
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
        String openId = session.getAttribute("openId") != null?session.getAttribute("openId").toString():"";
        if (StringUtils.isEmpty(openId)) {
            String inviteUrl = NET_WEB +"/login";
            inviteUrl = URLEncoder.encode(inviteUrl, "utf-8");
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + inviteUrl
                    + "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
            response.sendRedirect(url);
        }else{
            url= NET_WEB+"/index";
            response.sendRedirect(url);
        }
    }

    @RequestMapping("/login")
    public void login(HttpSession session,String code,HttpServletResponse response)throws IOException {

        log.info("系统开始，检查openId={}",session.getAttribute("openId"));

        TbUser user = WxUtils.oppenIdInfo(code,appId,appSecret,session);
        String open_id = (String)session.getAttribute("openId");

        if (StringUtils.isNotEmpty(open_id)) {
            List<TbUser> tbUsers = userService.queryUser(user);
            if (tbUsers.size() != 0) {
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                log.info("用户存在，则update");
                userService.updateTbUser(user);
            } else {
                log.info("用户不存在，则insert");
                user.setCreateTime(new Date());
                userService.insertTbUser(user);
            }
        }
        else {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            log.info("oppen_id==null");
        }
        String homeUrl = NET_WEB +"/index";
        response.sendRedirect(homeUrl);
    }
}
