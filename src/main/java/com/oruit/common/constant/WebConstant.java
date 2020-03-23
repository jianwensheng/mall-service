package com.oruit.common.constant;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class WebConstant implements InitializingBean {

    @Value("${ad.noshow.catalog}")
    private String adNoshowCatalog;

    @Value("${show.image.path}")
    private String showImagePath;

    @Value("${app.interface.secret}")
    private String appInterfaceScret;

    @Value("${app.used.url}")
    private String appUsedUrl;

    @Value("${app.interface.api.intranet.url}")
    private String appInterfaceApiIntranetUrl;

    @Value("${task.interface.api.intranet.url}")
    private String taskInterfaceApiIntranetUrl;

    @Value("${domain.interface.api.intranet.url}")
    private String domainInterfaceApiIntranetUrl;

    @Value("${oss.image.domain}")
    private String ossImageDomain;

    @Value("${common.api.key}")
    private String commonApiKey;

    @Value("${complaint_url}")
    private String complaintUrl;


    public static String ADNOSHOWCATALOG;
    public static String SHOWIMAGEPATH;
    public static String APPINTERFACESCRET;
    public static String APPUSEDURL;
    public static String APPINTERFACEAPIINTRANETURL;
    public static String TASKINTERFACEAPIINTRANETURL;
    public static String DOMAININTERFACEAPIINTRANETURL;
    public static String OSSIMAGEDOMAIN;
    public static String COMMONAPIKEY;
    public static String COMPLAINTURL;


    @Override
    public void afterPropertiesSet() throws Exception {
        ADNOSHOWCATALOG = adNoshowCatalog;
        SHOWIMAGEPATH = showImagePath;
        APPINTERFACESCRET = appInterfaceScret;
        APPUSEDURL = appUsedUrl;
        APPINTERFACEAPIINTRANETURL = appInterfaceApiIntranetUrl;
        TASKINTERFACEAPIINTRANETURL = taskInterfaceApiIntranetUrl;
        DOMAININTERFACEAPIINTRANETURL = domainInterfaceApiIntranetUrl;
        OSSIMAGEDOMAIN = ossImageDomain;
        COMMONAPIKEY = commonApiKey;
        COMPLAINTURL = complaintUrl;

    }
}
