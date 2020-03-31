package com.oruit.share.filter;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.constant.MallConstants;
import com.oruit.common.utils.StringUtils;
import com.oruit.share.cache.LoginCacheUtil;
import com.oruit.share.domain.Operation;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.OperationService;
import com.oruit.share.util.ApplicationContextUtil;
import com.oruit.share.util.CurrentLoginUtil;
import com.oruit.util.HttpResponseUtil;
import com.oruit.util.ResponseCode;
import com.oruit.util.SimpleResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//@Configuration
//@Order(1)
//@WebFilter(filterName="securityRequestFilter",urlPatterns="/mall/good/*")
@Slf4j
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("----------------------->过滤器被创建");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
