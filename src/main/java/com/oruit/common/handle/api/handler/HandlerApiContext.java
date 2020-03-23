package com.oruit.common.handle.api.handler;


import com.oruit.common.config.ApplicationContextRegister;

import java.util.Map;

/**
 * @author: wangyt
 * @date: 2019-08-23 10:17
 * @description: 处理器上下文，根据类型获取相应的处理器
 */
@SuppressWarnings("unchecked")
public class HandlerApiContext {

    private static Map<String, Class> handlerMap;

    public HandlerApiContext(Map<String, Class> handlerMap) {
        HandlerApiContext.handlerMap = handlerMap;
    }

    public static AbstractApiHandler getInstance(String type) {
        Class clazz = handlerMap.get(type);
        if (clazz == null) {
            return null;
        }
        return (AbstractApiHandler) ApplicationContextRegister.getBean(clazz);
    }

}
