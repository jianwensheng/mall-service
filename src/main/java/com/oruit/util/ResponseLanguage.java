package com.oruit.util;

import com.oruit.util.thread.HttpThreadLocal;
import com.oruit.util.thread.HttpThreadValue;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author huangjunfeng
 * @date 2016-02-05
 *
 */
public enum ResponseLanguage {

    ENGLISH("en", "", "responsecode.en.properties"),

    CHINESE("zh", "CN", "responsecode.zn_CN.properties"),

    JAPANESE("ja", "", "responsecode.ja.properties"),;

    private String language;

    private String region;

    public String propertiesFile;

    private static String defaultLanguage;

    private static String defaultRegion;

    private ResponseLanguage(String language, String region, String propertiesFile) {
        this.language = language;
        this.region = region;
        this.propertiesFile = propertiesFile;
    }

    public String language() {
        return language;
    }

    public String region() {
        return region;
    }

    @Override
    public String toString() {
        if (this.region == null || this.region.equals("")) {
            return this.language;
        } else {
            return this.language + "_" + this.region;
        }
    }

    public static String getDefaultLanguage() {
        if (defaultLanguage == null) {
            InputStream is = ResponseLanguage.class.getClassLoader().getResourceAsStream("conf.properties");
            if (is == null) {
                return null;
            }
            Properties properties = new Properties();
            try {
                properties.load(is);
                defaultLanguage = properties.getProperty("project.default.language");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultLanguage;
    }

    public static String getDefaultRegion() {
        if (defaultRegion == null) {
            InputStream is = ResponseLanguage.class.getClassLoader().getResourceAsStream("conf.properties");
            if (is == null) {
                return null;
            }
            Properties properties = new Properties();
            try {
                properties.load(is);
                defaultRegion = properties.getProperty("project.default.region");
                if (defaultRegion == null) {
                    defaultRegion = "";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return defaultRegion;
    }

    public static ResponseLanguage getInstance(HttpServletRequest request) {
        String language = null;
        String region = null;
        if (request != null) {
            language = request.getParameter("language");
            region = request.getParameter("region");
        }
        if(request != null) {
            String requestFrom = request.getHeader("Request-From");
            if ((!HttpCommonUtil.isEmpty(requestFrom) && "Analysys/Java".equals(requestFrom))
                    || !HttpCommonUtil.isEmpty(request.getHeader("Referer"))
                    || !HttpCommonUtil.isEmpty(request.getHeader("cache-control"))) {
                if ((language == null || language.equals("")) && request != null) {
                    language = request.getLocale().toString();
                    String strs[] = language.split("_");
                    if (strs.length > 0) {
                        language = strs[0];
                    }
                    if (strs.length > 1) {
                        region = strs[1];
                    }
                }
            }
        }

        if (language == null || "".equals(language)) {
            language = getDefaultLanguage();
            region = getDefaultRegion();
        }

        if (language == null || "".equals(language)) {
            return ResponseLanguage.CHINESE;
        }
        return getInstance(language, region);
    }

    public static ResponseLanguage getThreadLanguage() {
        HttpThreadValue httpThreadValue = HttpThreadLocal.getThreadValue();
        if (httpThreadValue != null) {
            return httpThreadValue.getThreadLanguage();
        }
        return ResponseLanguage.getInstance(HttpCommonUtil.getThreadRequest());
    }

    public static ResponseLanguage getInstance(String language, String region) {
        if (!HttpCommonUtil.isEmpty(language)) {
            if (language.indexOf("_") >= 0) {
                String[] arrs = language.split("_");
                if (arrs.length > 0) {
                    language = arrs[0];
                }
                if (arrs.length > 1) {
                    region = arrs[1];
                }
            } else if (language.indexOf("-") >= 0) {
                String[] arrs = language.split("-");
                if (arrs.length > 0) {
                    language = arrs[0];
                }
                if (arrs.length > 1) {
                    region = arrs[1];
                }
            }
        }

        ResponseLanguage[] languages = ResponseLanguage.values();
        for (ResponseLanguage lang : languages) {
            if (lang.language.equals(language) && lang.region.equals(region)) {
                return lang;
            }
        }
        for (ResponseLanguage lang : languages) {
            if (lang.language.equals(language)) {
                return lang;
            }
        }
        return ResponseLanguage.CHINESE;
    }

    public static void main(String[] args) {
        System.out.println(ResponseLanguage.getDefaultLanguage());
        System.out.println(ResponseLanguage.getDefaultRegion());
    }

}
