/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oruit.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * ip
 *
 * @author hanfeng
 */
@Slf4j
public class IPUtil {
    //将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    //将十进制整数形式转换成127.0.0.1形式的ip地址
    public static String longToIP(long longIp) {
        StringBuilder sb = new StringBuilder("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }


    /**
     * 获取当前的Ip地址
     *
     * @param request
     * @return
     */
    public static String getCurrentIP(HttpServletRequest request) {

        if (log.isDebugEnabled()) {
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                if (log.isDebugEnabled()) {
                    log.debug("header key={}", key);
                }
                Enumeration<String> values = request.getHeaders(key);
                while (values.hasMoreElements()) {
                    if (log.isDebugEnabled()) {
                        log.debug("header value={}", values.nextElement());
                    }
                }
            }
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (getIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (getIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (getIp(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (getIp(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (getIp(ip)) {
            ip = request.getRemoteAddr();
        }
        String[] ipArr = ip.split(",");
        if (ipArr.length > 0) {
            ip = ipArr[0];
        }
        return ip;
    }

    private static Boolean getIp(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }


    public static void main(String[] args) {
        String ipStr = "115.238.228.51";
        String[] dd = ipStr.split("\\|");
        //ip地址转化成二进制形式输出
        //if(logger.isDebugEnabled()){ logger.debug("二进制形式为：" + Long.toBinaryString(longIp)); }
    }
}
