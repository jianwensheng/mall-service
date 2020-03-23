package com.oruit.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;

/**
 * @author: wangyt
 * @date: 2019-08-28 10:35
 * @description: TODO
 */
public class TableNameUtils {

    /**
     * 如 表名_ggc_20100909
     *
     * @param tableNamePerfix
     * @param param
     * @return
     */
    public static String getTableName(String tableNamePerfix, String param) {
        return new StringBuilder().append(tableNamePerfix).append(param).append("_").append(DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN)).toString();
    }

    /**
     * 获取当天的表
     * 如：aaa_20190909
     *
     * @param tableNamePerfix
     * @return
     */
    public static String getTableName(String tableNamePerfix) {
        return new StringBuilder().append(tableNamePerfix).append(DateUtil.format(DateUtil.date(), DatePattern.PURE_DATE_PATTERN)).toString();
    }
}
