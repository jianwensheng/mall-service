package com.oruit.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParamMsg {

    public static final String NECESSARY_PARAM_PROPERTY_NAME = "necessary.param";
    public static final String MISSING_PARAM_PROPERTY_NAME = "missing.param";
    public static final String PARAMS_NAME = "param";
    public static final String NOT_CORRECT_NAME = "not.correct";

    public static String createParamNullMsg(String[] necessaryParamNames, List<String> missingParamNames) throws Exception {
        if (HttpCommonUtil.isEmptyArray(necessaryParamNames) && HttpCommonUtil.isEmptyList(missingParamNames)) {
            return null;
        }
        StringBuffer msg = new StringBuffer(ResponseCode.CODE_101_PARAM_NULL.msg(ResponseLanguage.getThreadLanguage()));
        // 参数为空，比传参数[],缺少参数[]
        if (!checkCreateParam(necessaryParamNames, missingParamNames)) {
            throw new Exception();
        } else {
            if (!HttpCommonUtil.isEmptyArray(necessaryParamNames)) {
                msg.append(";" + necessaryParamConstant());
                msg.append(" [");
                int i = 0;
                for (String np : necessaryParamNames) {
                    i++;
                    if (i == necessaryParamNames.length) {
                        msg.append(np);
                    } else {
                        msg.append(np + ",");
                    }
                }
                msg.append("] ");
            }

            if (!HttpCommonUtil.isEmptyList(missingParamNames)) {
                msg.append(";" + missingParamConstant());
                msg.append(" [");
                int i = 0;
                for (String mp : missingParamNames) {
                    i++;
                    if (i == missingParamNames.size()) {
                        msg.append(mp);
                    } else {
                        msg.append(mp + ",");
                    }
                }
                msg.append("] ");
            }
        }
        return msg.toString();
    }

    public static String createWrongParamMsg(String... paramNames) throws IOException {
        if (HttpCommonUtil.isEmptyArray(paramNames)) {
            return null;
        }
        StringBuffer msg = new StringBuffer(ResponseCode.PARAM_WRONG_100.msg(ResponseLanguage.getThreadLanguage()));
        msg.append(";" + HttpPropUtil.getThreadString(PARAMS_NAME) + " [");
        int i = 0;
        for (String mp : paramNames) {
            i++;
            if (i == paramNames.length) {
                msg.append(mp);
            } else {
                msg.append(mp + ",");
            }
        }
        msg.append("] " + HttpPropUtil.getThreadString(NOT_CORRECT_NAME));
        return msg.toString();
    }

    private static boolean checkCreateParam(String[] necessaryParamNames, List<String> missingParamNames) {
        return true;
    }

    private static String necessaryParamConstant() throws IOException {
        return HttpPropUtil.getInstance(ResponseLanguage.getThreadLanguage().propertiesFile)
                .getString(NECESSARY_PARAM_PROPERTY_NAME);
    }

    private static String missingParamConstant() throws IOException {
        return HttpPropUtil.getInstance(ResponseLanguage.getThreadLanguage().propertiesFile)
                .getString(MISSING_PARAM_PROPERTY_NAME);
    }

    public static void main(String[] args) throws Exception {
        String[] necessaryParamNames = { "casId", "startTime", "endTime", "indexId" };
        List<String> missingParamNames = new ArrayList<String>();
        missingParamNames.add("casId");
        missingParamNames.add("startTime");
        // System.out.println(createParamNullMsg(necessaryParamNames,
        // missingParamNames));
        System.out.println(createWrongParamMsg(necessaryParamNames));
    }
}
