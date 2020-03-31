package com.oruit.util.thread;

import com.oruit.util.ResponseLanguage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

public class HttpThreadValue implements Serializable {


    private static final long serialVersionUID = 1L;

    private ResponseLanguage threadLanguage;

    private HttpServletRequest request;

    private HttpServletResponse response;

    public HttpThreadValue(ResponseLanguage threadLanguage, HttpServletRequest request, HttpServletResponse response) {
        notNull(threadLanguage, "参数 threadLanguage不能为空");
        notNull(request, "参数 request不能为空");
        notNull(response, "参数 response不能为空");
        this.threadLanguage = threadLanguage;
        this.request = request;
        this.response = response;
    }

    public ResponseLanguage getThreadLanguage() {
        return threadLanguage;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    private void notNull(final Object object, final String message) {
        if (object == null)
            throw new IllegalArgumentException(message);
    }

}
