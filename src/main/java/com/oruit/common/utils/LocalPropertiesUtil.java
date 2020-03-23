package com.oruit.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class LocalPropertiesUtil {
    private static Properties properties = null;
    private static Object lock = new Object();
    public static final String PROPERTIES_FILE = "/application.properties";

    public static String getString(String key) {
        Properties p = initProperties();
        if(p == null) {
        	return null;
        }
        return p.getProperty(key);
    }

    public static boolean getBoolean(String key) {
        Properties p = initProperties();
        String value = p.getProperty(key);
        boolean result = false;
        try {
            result = Boolean.valueOf(value).booleanValue();
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public static String getString(String key, String defaultValue) {
        Properties properties = initProperties();
        return properties.getProperty(key, defaultValue);
    }

    private static Properties initProperties() {
        if (properties == null) {
            synchronized (lock) {
                if (properties == null) {
                    properties = new Properties();
                    try {
                        String CONF_HOME = System.getProperty("CONF_HOME");
                        CONF_HOME = CONF_HOME == null ? "" : CONF_HOME;
                        
                        File file = new File(LocalPropertiesUtil.class.getResource("/application.properties").getPath());
                        if(!file.exists()) {
                            return null;
                        }
                        properties.load(LocalPropertiesUtil.class.getResourceAsStream("/application.properties"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return properties;
    }
}
