package com.oruit.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

public class ParamCheckResponseUtil {

    /**
     * <b>注意:paramNames必须与paramValues一一对应</b>
     * 
     * @param paramNames
     * @param paramValues
     * @return Map<String,Object>
     * @throws Exception
     */
    public static Map<String, Object> getMapForNecessaryParams(String[] paramNames, Object... paramValues) throws Exception {

        if ((HttpCommonUtil.isEmptyArray(paramNames)) && (HttpCommonUtil.isEmptyArray(paramValues))) {
            return null;
        }

        if (HttpCommonUtil.isEmptyArray(paramNames) && !HttpCommonUtil.isEmptyArray(paramValues)) {
            throw new Exception("paramNames与paramValues不一致");
        }

        if (!HttpCommonUtil.isEmptyArray(paramNames) && HttpCommonUtil.isEmptyArray(paramValues)) {
            throw new Exception("paramNames与paramValues不一致");
        }
        if (paramNames.length != paramValues.length) {
            throw new Exception("paramNames与paramValues不一致");
        }

        List<String> missingList = new ArrayList<String>();

        for (int i = 0; i < paramNames.length; i++) {
            Object val = paramValues[i];
            if (val == null || (val instanceof String && "{}".equals((String) val))
                    || (val instanceof String && StringUtils.isBlank((String) val))) {
                missingList.add(paramNames[i]);
            }
        }
        if (HttpCommonUtil.isEmptyList(missingList)) {
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ResponseCode.CODE.value(), ResponseCode.CODE_101_PARAM_NULL.code());
            map.put(ResponseCode.MSG.value(), ParamMsg.createParamNullMsg(paramNames, missingList));
            return map;
        }

    }

    public static Map<String, Object> getMapForNecessaryParams(String... paramNames) throws Exception {
        if (paramNames == null || paramNames.length == 0) {
            return null;
        }
        List<String> missingList = new ArrayList<String>();
        for (String paramName : paramNames) {
            HttpServletRequest request = HttpCommonUtil.getThreadRequest();
            if (request == null) {
                throw new Exception("该方法只能应用于处理http请求");
            }
            String paramValue = request.getParameter(paramName);
            if (HttpCommonUtil.isEmpty(paramValue) || paramValue.equals("{}")) {
                missingList.add(paramName);
            }
        }
        if (HttpCommonUtil.isEmptyList(missingList)) {
            return null;
        } else {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ResponseCode.CODE.value(), ResponseCode.CODE_101_PARAM_NULL.code());
            map.put(ResponseCode.MSG.value(), ParamMsg.createParamNullMsg(paramNames, missingList));
            return map;
        }
    }

    public static Map<String, Object> getMapForWrongParam(String... paramNames) throws IOException {
        if (paramNames == null || paramNames.length == 0) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ResponseCode.CODE.value(), ResponseCode.PARAM_WRONG_100.code());
        map.put(ResponseCode.MSG.value(), ParamMsg.createWrongParamMsg(paramNames));
        return map;
    }

    public static void main(String[] args) throws Exception {
        try {
            System.out.println(JSONObject.toJSONString(getMapForWrongParam(new String[] { "casId", "starTime" })));
            String casId = "";
            Date startTime = new Date();
            System.out.println(JSONObject.toJSONString(getMapForNecessaryParams(new String[] { "casId", "starTime" },casId,startTime)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
