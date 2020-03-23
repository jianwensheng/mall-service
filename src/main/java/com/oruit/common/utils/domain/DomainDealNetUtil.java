/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oruit.common.utils.domain;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.constant.WebConstant;
import com.oruit.common.utils.web.RequestUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hanfeng
 */
@Slf4j
public class DomainDealNetUtil {

    /**
     * 根据原始URL获取展示的URL规则
     *
     * @param originalUrl
     * @param urlType
     * @return
     */
    public String GetShowUrlFromOriginalUrl(String originalUrl, Integer urlType) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("original_url", originalUrl);
        params.put("url_type", urlType);
        params.put("Method", "GetShowUrlFromOriginalUrl");
        JSONObject jsonShowUrl = netRequestCommonDeal(params);
        if (jsonShowUrl != null) {
            return jsonShowUrl.getString("showurl");
        }
        return "";
    }

    /**
     * 获取普通文章的展示域名
     *
     * @param articleid
     * @param curdomain
     * @return
     */
    public JSONObject GetArticleShowDomain(Integer articleid, String curdomain, String source) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("articleid", articleid);
        params.put("curdomain", curdomain);
        params.put("source", source);
        params.put("Method", "GetArticleShowDomain");
        return netRequestCommonDeal(params);
    }

    /**
     * 获取广告的展示域名
     *
     * @param articleid
     * @param curdomain
     * @return
     */
    public JSONObject GetADShowDomain(Integer articleid, String curdomain, String source) {
        Map<String, Object> params = new HashMap<>(16);
        params.put("articleid", articleid);
        params.put("curdomain", curdomain);
        params.put("source", source);
        params.put("Method", "GetADShowDomain");
        return netRequestCommonDeal(params);
    }

    /**
     * 查询详情页的广告的展示域名
     *
     * @param articleid
     * @param source
     * @return
     */
    public JSONObject GetArticlePageADShareDomain(Integer articleid, String source) {
        Map<String, Object> params = new HashMap<>();
        params.put("articleid", articleid);
        params.put("source", source);
        params.put("Method", "GetADShareDomain");
        return netRequestCommonDeal(params);
    }

    /**
     * 根据展示URL获取实际处理的URL
     *
     * @param showUrl
     * @return
     */
    public String GetOriginalFromUrlShowUrl(String showUrl) {
        Map<String, Object> params = new HashMap<>();
        params.put("show_url", showUrl);
        params.put("Method", "GetOriginalUrlFromShowUrl");
        JSONObject jsonShowUrl = netRequestCommonDeal(params);
        if (jsonShowUrl != null) {
            return jsonShowUrl.getString("original_url");
        }
        return "";
    }

    /**
     * 网络请求的通用方法
     *
     * @param params
     * @return
     */
    private JSONObject netRequestCommonDeal(Map<String, Object> params) {
        params.put("Infversion", "1.0");
        params.put("UID", System.currentTimeMillis());
        params.put("Key", WebConstant.COMMONAPIKEY);
        try {
            String str = RequestUtils.baseRequestPostJSONParams(WebConstant.DOMAININTERFACEAPIINTRANETURL, JSONObject.toJSONString(params));
            JSONObject jsonResult = JSONObject.parseObject(str);
            String code = jsonResult.getString("code");
            if ("1000".equals(code)) {
                return JSONObject.parseObject(jsonResult.getString("data"));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
