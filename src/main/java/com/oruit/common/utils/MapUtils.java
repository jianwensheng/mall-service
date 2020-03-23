package com.oruit.common.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class MapUtils extends MapUtil {

    /**
     * 参数给实体类赋值
     *
     * @param params
     * @param clazz
     * @throws IllegalAccessException
     */
    public static void mapToBean(Map<String, Object> params, Class clazz) throws IllegalAccessException {
        if (params == null) {
            return;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String simpleName = field.getType().getSimpleName().toLowerCase();
            System.out.println(simpleName);
            String name = field.getName();
            if (!params.containsKey(name)) {
                continue;
            }
            Object o = params.get(name);
            String value = "";
            if (ObjectUtil.isNotEmpty(o)) {
                value = o.toString();
            }
            //防止出现空指针全部使用 string
            switch (simpleName) {
                case "string":
                    field.set(name, value);
                    break;
                default:
                    break;
            }
            if (log.isDebugEnabled()) {
                log.debug("name:{},type:{},value:{}", name, simpleName, field.get(field.getName()));
            }
        }
    }
}
