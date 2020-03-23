package com.oruit.share.service.impl.handler.api;


import com.oruit.common.config.ApplicationContextRegister;

import java.util.Map;

/**
 * @author: wangyt
 * @date: 2019-08-23 10:17
 * @description: 处理器上下文，根据类型获取相应的处理器
 */
@SuppressWarnings("unchecked")
public class HandlerAdContext {

    private static Map<String, Class> handlerMap;

    public HandlerAdContext(Map<String, Class> handlerMap) {
        HandlerAdContext.handlerMap = handlerMap;
    }

    public static AbstractAdHandler getInstance(String type) {
        Class clazz = handlerMap.get(type);
        if (clazz == null) {
            return null;
        }
        return (AbstractAdHandler) ApplicationContextRegister.getBean(clazz);
    }

}
