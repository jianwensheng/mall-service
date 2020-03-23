package com.oruit.common.handle.api.common;


import com.oruit.common.handle.api.handler.AbstractApiHandler;
import com.oruit.common.handle.api.handler.ApiHandlerType;
import com.oruit.common.handle.api.utils.CommonUtils;
import org.springframework.stereotype.Component;

/**
 * @author: wangyt
 * @date: 2019-08-23 10:17
 * @description: TODO
 */
@Component
@ApiHandlerType({CommonUtils.MORE})
public class CommonMore extends AbstractApiHandler {
    @Override
    public String handle() {
        return CommonUtils.MORE;
    }
}


