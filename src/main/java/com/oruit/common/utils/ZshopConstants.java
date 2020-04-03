package com.oruit.common.utils;

import lombok.Data;

@Data
public class ZshopConstants {

    public static String JD_PREFIX;

    public static String PDD_PREFIX;

    public static String TAOBAO_PREFIX;

    public static String MOBILE_HOST;

    public static  class UploadFilesConstants{

        public static Boolean getIsWindowSystem(){
            String name = System.getProperties().getProperty("os.name");
            if(name.toLowerCase().contains("windows")){
                return true;
            }
            return false;
        }

        public  static String STATICFILESTEMPPATH = getIsWindowSystem()?"D:/images/":"/opt/java/file/";
    }
}
