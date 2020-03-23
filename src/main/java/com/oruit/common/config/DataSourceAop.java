package com.oruit.common.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 定义切面切点
 *
 * @author:wabgyt
 * @date 2019年8月30日
 */
@Aspect
@Component
@Slf4j
public class DataSourceAop {
    @Before("execution(* com.oruit.share.dao.*.select*(..)) " +
            "|| execution(* com.oruit.share.dao.*.count*(..)) " +
            "|| execution(* com.oruit.share.dao.*.get*(..)) " +
            "|| execution(* com.oruit.share.dao.*.find*(..)) " +
            "|| execution(* com.oruit.share.dao.*.check*(..)) " +
            "|| execution(* com.oruit.share.dao.*.load*(..)) " +
            "|| execution(* com.oruit.share.dao.*.query*(..)) " +
            "|| execution(* com.oruit.share.dao.*.search*(..))")

    public void setReadDataSourceType() {
        DataSourceContextHolder.read();
        if (log.isDebugEnabled()) {
            log.info("dataSource切换到：Read");
        }
    }

    @Before("execution(* com.oruit.share.dao.*.insert*(..)) " +
            "|| execution(* com.oruit.share.dao.*.update*(..)) " +
            "|| execution(* com.oruit.share.dao.*.delete*(..))" +
            "|| execution(* com.oruit.share.dao.*.add*(..))" +
            "|| execution(* com.oruit.share.dao.*.append*(..))" +
            "|| execution(* com.oruit.share.dao.*.save*(..))" +
            "|| execution(* com.oruit.share.dao.*.modify*(..))" +
            "|| execution(* com.oruit.share.dao.*.edit*(..))" +
            "|| execution(* com.oruit.share.dao.*.remove*(..))" +
            "|| execution(* com.oruit.share.dao.*.repair*(..))")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.write();
        if (log.isDebugEnabled()) {
            log.info("dataSource切换到：write");
        }
    }
}
