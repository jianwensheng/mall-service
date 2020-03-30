package com.oruit.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponseUtil {

	static final Logger log = LoggerFactory.getLogger(HttpCommonUtil.class);

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    // 每次response的resMap来自 线程容器，保障线程安全
    private static Map<String, Object> getResMap() {
        Map<String, Object> resMap = threadLocal.get();
        if (resMap == null) {
            resMap = new HashMap<String, Object>();
            threadLocal.set(resMap);
        }
        return resMap;
    }

    private static void setResMap(Map<String, Object> resMap) {
        threadLocal.remove();
        threadLocal.set(resMap);
    }

    public static StandardResult getStandardResultByCode(ResponseCode responseCode, Object datas,
            ResponseLanguage responseLanguage) {
        Map<String, Object> map = getResponseMapByCode(responseCode, datas, responseLanguage);
        return JSONObject.parseObject(JSONObject.toJSONString(map), StandardResult.class);
    }

    public static StandardResult getStandardResultByCode(ResponseCode responseCode, Object datas) {
        return getStandardResultByCode(responseCode, datas, null);
    }

    public static StandardResult getStandardResultByCode(ResponseCode responseCode, ResponseLanguage responseLanguage) {
        return getStandardResultByCode(responseCode, null, responseLanguage);
    }

    public static StandardResult getStandardResultByCode(ResponseCode responseCode) {
        return getStandardResultByCode(responseCode, null, null);
    }

    /**
     * 返回Map集合，例：<br>
     * Map<String,Object> map =
     * HttpResponseUtil.getResponseMapByCode(ResponseCode.SUCCESS_0);
     * 
     * @param responseCode
     * @return
     */
    public static Map<String, Object> getResponseMapByCode(ResponseCode responseCode) {

        return getResponseMapByCode(responseCode, null, null);
    }

    public static Map<String, Object> getResponseMapByCode(ResponseCode responseCode, Object datas) {
        return getResponseMapByCode(responseCode, datas, null);
    }

    public static Map<String, Object> getResponseMapByCode(ResponseCode responseCode, ResponseLanguage responseLanguage) {

        return getResponseMapByCode(responseCode, null, responseLanguage);
    }

    public static Map<String, Object> getResponseMapByCode(ResponseCode responseCode, Object datas,
            ResponseLanguage responseLanguage) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ResponseCode.CODE.value(), responseCode.code());
        map.put(ResponseCode.MSG.value(), responseCode.msg(responseLanguage));
        if (datas != null) {
            map.put(ResponseCode.DATAS.value(), datas);
        }
        return map;
    }

    public static void sendJson(HttpServletResponse response, String content) {
        if (StringUtils.isBlank(content))
            return;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.append(content);
        } catch (IOException e) {
            log.error("发送json数据异常，要发送的内容是：" + content);
        } finally {
            getResMap().clear();
            threadLocal.remove();
        }
    }

    public static void sendJsonByCode(HttpServletResponse response, ResponseCode responseCode, Object datas, boolean isFilterNull,
                                      ResponseLanguage responseLanguage) {
        setCode(responseCode, responseLanguage);
        if (datas != null) {
            getResMap().put(ResponseCode.DATAS.value(), datas);
        }
        String content = "";
        if (isFilterNull) {
            content = JSON.toJSONString(getResMap());
        } else {
            content = JSON.toJSONString(getResMap(), SerializerFeature.WriteMapNullValue,
            		SerializerFeature.DisableCircularReferenceDetect);
        }
        sendJson(response, content);
    }

    public static void sendJsonByCode(HttpServletResponse response, ResponseCode responseCode, Object datas,
                                      ResponseLanguage responseLanguage) {
        sendJsonByCode(response, responseCode, datas, false, responseLanguage);
    }

    public static void sendJsonByCode(HttpServletResponse response, ResponseCode responseCode, Object datas,
                                      boolean isFilterNull) {
        sendJsonByCode(response, responseCode, datas, isFilterNull, null);
    }

    public static void sendJsonByCode(HttpServletResponse response, ResponseCode responseCode, Object datas) {
        sendJsonByCode(response, responseCode, datas, false);
    }

    public static void sendJsonByCode(HttpServletResponse response, ResponseCode responseCode,
                                      ResponseLanguage responseLanguage) {
        sendJsonByCode(response, responseCode, null, false, responseLanguage);
    }

    public static void sendJsonByCode(HttpServletResponse response, ResponseCode responseCode) {
        sendJsonByCode(response, responseCode, null);
    }

    public static void sendSuccessJson(HttpServletResponse response, Object datas, boolean isFilterNull) {
        sendJsonByCode(response, ResponseCode.SUCCESS_0, datas, false);
    }

    public static void sendSuccessJson(HttpServletResponse response, Object datas) {
        sendSuccessJson(response, datas, false);
    }

    public static void sendSuccessJson(HttpServletResponse response) {
        sendSuccessJson(response, null, false);
    }

    /**
     * 用于对参数不能为空的判断，返回的结果中code只能为101，msg自定义
     * 
     * @param tips
     * @param params
     * @return Map
     */
    public static Map<String, Object> checkNecessaryParams(String tips, Object... params) {
        boolean checkRet = true;
        int index = 0;
        List<Integer> lostParamIndexs = new ArrayList<Integer>();
        for (Object param : params) {
            index++;
            if (param == null) {
                checkRet = false;
                lostParamIndexs.add(index);
            }
            // 判断是否为json格式的空字符串
            else if (param instanceof String && "{}".equals((String) param)) {
                checkRet = false;
                lostParamIndexs.add(index);
            } else if (param instanceof String && StringUtils.isBlank((String) param)) {
                checkRet = false;
                lostParamIndexs.add(index);
            }
        }
        Map<String, Object> m = new HashMap<String, Object>();
        if (checkRet == false) {
            if (tips != null && tips.startsWith("params[") && tips.endsWith("]")) {
                String paramNameStrs = tips.substring(7, tips.length() - 1);
                String[] paramNames = paramNameStrs.split(",");
                String lostParams = "";
                for (Integer lostParamIndex : lostParamIndexs) {
                    if (paramNames != null && paramNames.length >= lostParamIndex)
                        lostParams = lostParams + paramNames[lostParamIndex - 1] + ",";
                }
                if (StringUtils.isNotBlank(lostParams)) {
                    lostParams = lostParams.substring(0, lostParams.length() - 1);
                    tips = "传参错误，必传参数" + tips + "，缺少参数lostParams[" + lostParams + "]";
                }
            }
            m.put(ResponseCode.CODE.value(), ResponseCode.CODE_101_PARAM_NULL.code());
            m.put(ResponseCode.MSG.value(), tips == null ? "" : tips);
        }

        if (checkRet) {
            return null;
        }
        return m;
    }

    /**
     * 用于对参数不能为空的判断，返回的结果中code只能为101，msg自定义
     * 
     * @param tips
     * @param params
     * @return StandardResult
     */
    public static StandardResult getStandardResultForParamNull(String tips, Object... params) {
        Map<String, Object> map = checkNecessaryParams(tips, params);
        if (map == null) {
            return null;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(map), StandardResult.class);
    }

    /**
     * 用于对参数不能为空的判断，返回的结果中code只能为101，msg自定义
     * 
     * @param tips
     * @param params
     * @return StandardResult
     */
    public static void sendJsonForParamNull(HttpServletResponse response, String tips, Object... params) throws Exception {
        Map<String, Object> map = checkNecessaryParams(tips, params);
        if (map == null) {
            throw new Exception("参数均不为空");
        } else {
            sendJson(response, JSONObject.toJSONString(map));
        }
    }

    /**
     * msg:参数为空，比传参数[],缺少参数[]
     * 
     * @param responseCode
     * @param responseLanguage
     */

    /**
     * msg:参数错误，出错参数[]
     * 
     * @param responseCode
     * @param responseLanguage
     */

    /*
     * public static void addToDatas(String name, Object value) { if(name ==
     * null || "".equals(name) || value == null) { return; } Object datas =
     * getResMap().get(ResponseCode.DATAS.value()); if(datas == null) {
     * getResMap().put(ResponseCode.DATAS.value(), value); } else { String
     * jsonString = JSONObject.toJSONString(datas); JSONObject jsonObject =
     * JSON.parseObject(jsonString); jsonObject.put(name, value);
     * getResMap().put(ResponseCode.DATAS.value(), jsonObject); } }
     */

    /*
     * public static void setDatas(Object datas) { if (datas != null) {
     * getResMap().put(ResponseCode.DATAS.value(), datas); } }
     */

    private static void setCode(ResponseCode responseCode, ResponseLanguage responseLanguage) {
        if (responseCode == null) {
            return;
        }
        getResMap().put(ResponseCode.CODE.value(), responseCode.code());
        getResMap().put(ResponseCode.MSG.value(), responseCode.msg(responseLanguage));
    }

    private static void setCode(ResponseCode responseCode) {
        if (responseCode == null) {
            return;
        }
        getResMap().put(ResponseCode.CODE.value(), responseCode.code());
        getResMap().put(ResponseCode.MSG.value(), responseCode.msg(null));
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.isBlank(null));
    }
}
