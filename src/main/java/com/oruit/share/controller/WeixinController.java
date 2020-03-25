package com.oruit.share.controller;

import com.oruit.share.service.WeixinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Controller
@RequestMapping("wx")
@Slf4j
public class WeixinController {

    @Autowired
    WeixinService weixinService;

    @RequestMapping(value = {"check"}, method = {RequestMethod.GET})
    public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String signature = request.getParameter("signature");

        String timestamp = request.getParameter("timestamp");

        String nonce = request.getParameter("nonce");

        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();

        if (weixinService.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
            log.info("微信服務驗證成功!");
        }
        out.close();
        out = null;
    }

}
