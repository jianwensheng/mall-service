package com.oruit.common.filter;

import cn.hutool.core.util.StrUtil;
import com.oruit.common.handle.api.handler.AbstractApiHandler;
import com.oruit.common.handle.api.handler.HandlerApiContext;
import com.oruit.common.handle.api.utils.UrlConfig;
import com.oruit.common.utils.domain.DomainDealNetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * @author: wangyt
 * @date: 2019-08-27 10:08
 * @description: TODO
 */
@Slf4j
public class APIFilter extends OncePerRequestFilter {
    private static final Boolean DEBUGENABLED = log.isDebugEnabled();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();
       /* if (DEBUGENABLED) {
            log.debug("requestUrl:{}|queryString:{}", requestUrl, queryString);
        }*/


        //添加协议头已支持跨域请求
        response.addHeader("Access-Control-Allow-Origin", "*");

        filterChain.doFilter(request, response);
    }


}
