package com.oruit.common.config;

import com.oruit.common.utils.web.RequestUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PathInterceptor implements HandlerInterceptor {
    public static final String CONTEXT_PATH = "ctx";
    public static final String BASE_PATH = "basePath";
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        httpServletRequest.setAttribute(CONTEXT_PATH, httpServletRequest.getContextPath());
        httpServletRequest.setAttribute(BASE_PATH, RequestUtils.getBasePath(httpServletRequest));
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
