package com.oruit.share.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("order")
@Slf4j
public class OrderController {

    @RequestMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        return "order_list";
    }

}
