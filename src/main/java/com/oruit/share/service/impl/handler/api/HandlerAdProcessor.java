package com.oruit.share.service.impl.handler.api;


import com.oruit.common.handle.api.utils.ClassScaner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@SuppressWarnings("unchecked")
public class HandlerAdProcessor implements BeanFactoryPostProcessor {

    private static final String HANDLER_PACKAGE = "com.oruit.share.service.impl.handler";

    /**
     * 扫描@HandlerType，初始化HandlerContext，将其注册到spring容器
     *
     * @param beanFactory bean工厂
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, Class> handlerMap = new HashMap<>();
        ClassScaner.scan(HANDLER_PACKAGE, AdHandlerType.class).forEach(clazz -> {
            String[] type = clazz.getAnnotation(AdHandlerType.class).value();
            for (int i = 0; i < type.length; i++) {
                handlerMap.put(type[i], clazz);

            }
        });
        HandlerAdContext context = new HandlerAdContext(handlerMap);
        beanFactory.registerSingleton(HandlerAdContext.class.getName(), context);
    }

}
