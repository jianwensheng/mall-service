package com.oruit.share.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("guide")
@Slf4j
public class GuidanceController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        return "guidance";
    }

    /**
     * 淘宝省钱步骤
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/tbSaveMoney")
    public String tbSaveMoney(HttpServletRequest request, Model model) {
        return "index-class-tb";
    }

    /**
     * 拼多多省钱步骤
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/pddSaveMoney")
    public String pddSaveMoney(HttpServletRequest request, Model model) {
        return "index-class-tb";
    }
}
