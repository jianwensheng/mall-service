package com.oruit.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HttpPropUtil {

    private Properties properties;

    private static Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

    public static HttpPropUtil getInstance(String propertiesFile) throws IOException {
        if (propertiesFile != null && "".equals(propertiesFile)) {
            return null;
        }
        Properties prop = propertiesMap.get(propertiesFile);
        if (prop == null) {
            InputStream in = HttpPropUtil.class.getResourceAsStream("/" + propertiesFile);
            if (in != null) {
                HttpPropUtil httpPropUtil = new HttpPropUtil();
                httpPropUtil.properties = new Properties();
                httpPropUtil.properties.load(in);
                propertiesMap.put(propertiesFile, httpPropUtil.properties);
                return httpPropUtil;
            }
        } else {
            HttpPropUtil httpPropUtil = new HttpPropUtil();
            httpPropUtil.properties = prop;
            return httpPropUtil;
        }

        return null;
    }

    public static String getThreadString(String key) throws IOException {
        return getInstance(ResponseLanguage.getThreadLanguage().propertiesFile).getString(key);
    }

    private HttpPropUtil() {

    }

    public String getString(String key) {
        if (properties == null) {
            return null;
        }
        return properties.getProperty(key);
    }

    public static void main(String[] args) {
        StandardResult result = HttpResponseUtil.getStandardResultByCode(ResponseCode.SUCCESS_0, ResponseLanguage.ENGLISH);
        System.out.println(result.toString());
    }
}
