package com.oruit.common.handle.api.handler;


import com.oruit.common.handle.api.utils.ClassScaner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@SuppressWarnings("unchecked")
public class HandlerApiProcessor implements BeanFactoryPostProcessor {

    private static final String HANDLER_PACKAGE = "com.oruit.common.handle.api";

    /**
     * 扫描@HandlerType，初始化HandlerContext，将其注册到spring容器
     *
     * @param beanFactory bean工厂
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Class> handlerMap = new HashMap<>(16);
        ClassScaner.scan(HANDLER_PACKAGE, ApiHandlerType.class).forEach(clazz -> {
            String[] type = clazz.getAnnotation(ApiHandlerType.class).value();
            for (int i = 0; i < type.length; i++) {
                handlerMap.put(type[i], clazz);

            }
        });
        HandlerApiContext context = new HandlerApiContext(handlerMap);
        beanFactory.registerSingleton(HandlerApiContext.class.getName(), context);
    }

}
