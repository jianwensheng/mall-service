package com.oruit.common.handle.api.handler;

import java.lang.annotation.*;


/**
 * @author: wangyt
 * @date: 2019-08-23 10:17
 * @description: TODO
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiHandlerType {

    String[] value();

}
