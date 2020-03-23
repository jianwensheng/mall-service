package com.oruit.share.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: xc
 * @date: 2019-09-28 10:09
 * @description: TODO
 */
public interface AdTransactionService {

    /**
     * 成交的订单转化需要记录是哪个人分享的哪篇文章引流的
     * @return
     */
    void adDeall(HttpServletRequest request) throws Exception;
}
