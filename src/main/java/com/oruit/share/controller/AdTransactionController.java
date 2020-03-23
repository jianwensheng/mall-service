package com.oruit.share.controller;

import com.oruit.common.utils.WebUtil;
import com.oruit.share.service.AdTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName AdTransactionController
 * @Description TODO  文章回调记录接口
 * @Author xc
 * @Date 2019/9/28 10:06
 * @Version 1.0
 **/
@RestController
@RequestMapping("adtransaction")
@Slf4j
public class AdTransactionController {

    @Autowired
    private AdTransactionService adTransactionService;


    @GetMapping("deall")
    public void deall(HttpServletRequest request) {
        Map<String, Object> params = WebUtil.getParams(request);
        if (log.isDebugEnabled()) {
            log.debug("method:adtransaction deall,params:{}", params);
        }
        try {
            adTransactionService.adDeall(request);
        } catch (Exception e) {
           log.error("adtransaction deall:{}",e.getMessage());
        }
    }
}
