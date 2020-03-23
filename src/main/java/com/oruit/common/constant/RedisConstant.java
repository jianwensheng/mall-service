package com.oruit.common.constant;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: wangyt
 * @date: 2019-08-27 13:58
 * @description: TODO
 */
@Data
@Component
@PropertySource("classpath:config.properties")
@ConfigurationProperties(prefix = "redis")
@Slf4j
public class RedisConstant implements InitializingBean {

    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private Integer port;

    @Value("${redis.password}")
    private String password;

    public static String HOST;
    public static Integer PORT;
    public static String USERNAME;
    public static String PASSWORD;


    @Override
    public void afterPropertiesSet() throws Exception {
        HOST = host;
        PASSWORD = password;
        PORT = port;
    }
}
