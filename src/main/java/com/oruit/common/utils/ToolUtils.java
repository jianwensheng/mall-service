/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oruit.common.utils;

import net.sf.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hanfeng
 */
public class ToolUtils {
       public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map =new HashMap(16);
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println("我");
        System.out.println("     的");
        System.out.println("世");
        System.out.println("界");
    }
}
