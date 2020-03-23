package com.oruit.common.config;

import com.oruit.common.filter.APIFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author: wangyt
 * @date: 2019-08-27 10:06
 * @description: TODO
 */
@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean apiFilter(){
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new APIFilter());
        registrationBean.setUrlPatterns(Arrays.asList("/*"));
        // 过滤器的优先级
        registrationBean.setOrder(1);
        return registrationBean;

    }
}
