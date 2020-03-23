/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oruit.common.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 时间通用类
 *
 * @author Liuk
 */
public class DateUtil {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 时间规则：
     * <p>
     * <2分钟：刚刚
     * <p>
     * =今天：今天 HH:mm
     * <p>
     * =昨天：昨天 HH:mm
     * <p>
     * =今年 : MM月dd日 HH:mm
     * <p>
     * <今年：yyyy/MM/dd HH:mm
     * <p>
     * FFF @param createTime
     *
     * @param createTime
     * @return
     * @throws ParseException
     * @ return
     */
    public static String getTimes(String createTime) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date date = df.parse(createTime);
        long l = now.getTime() - date.getTime();
        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        if (logger.isDebugEnabled()) {
            logger.debug("------------" + day + "==============" + createTime);
        }
        if (now.getDate() - date.getDate() == 0 && hour == 0 && min <= 2) {
            return "刚刚";
        } else if (now.getDate() - date.getDate() == 0) {
            return "今天 " + createTime.substring(11, 16);
        } else if (now.getDate() - date.getDate() == 1) {
            return "昨天 " + createTime.substring(11, 16);
        } else if (now.getYear() == date.getYear()) {
            return "今年 " + (date.getMonth() + 1) + "月" + date.getDate() + "日" + createTime.substring(11, 16);
        } else {
            return createTime;
        }
    }

    @SuppressWarnings("empty-statement")
    public static String getZHDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        ;
        return sdf.format(new Date());
    }

    /**
     * 格式化日期字符串
     *
     * @param format
     * @return
     */
    public static String formatDate(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    public static String getZHDate(String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = df.parse(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.format(date1);
    }

    /**
     * 获取几天后的时间
     *
     * @param d
     * @param day 几天后
     * @return
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /**
     * 获取几小时后时间
     *
     * @param d
     * @param hour 几小时后
     * @return
     */
    public static Date getHourAfter(Date d, int hour) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) + hour);
        return now.getTime();
    }

    /**
     * 多少秒以后
     *
     * @param d
     * @param minute
     * @return
     */
    public static Date getMinuteAfter(Date d, int minute) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + minute);
        return now.getTime();
    }

    /**
     * 比较时间大小--比较当前时间和传入时间（HH：mm）大小
     *
     * @param time
     * @return
     */
    public static long compareTimeToNow(String time) {
        try {
            long nowM = System.currentTimeMillis();
            Date date = new Date(nowM);
            SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
            String nowTime = sf.format(date);
            date = sf.parse(nowTime);
            nowM = date.getTime();

            Date comTime = sf.parse(time);
            return nowM - comTime.getTime();
        } catch (Exception ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * 比较时间大小--比较是否为同一天
     *
     * @param comDate
     * @return 小于0： 大于当前时间 等于0： 和当前时间相同 大于0：小于当前时间
     */
    public static long compareDayToNow(Date comDate) {
        return compareDay(new Date(), comDate);
    }

    /**
     * 比较时间大小--比较是否为同一天
     *
     * @param currentDate
     * @param comDate
     * @return 小于0： 大于当前时间 等于0： 和当前时间相同 大于0：小于当前时间
     */
    public static long compareDay(Date currentDate, Date comDate) {
        try {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            String nowTime = sf.format(currentDate);
            String comTime = sf.format(comDate);
            if (nowTime.equals(comTime)) {
                return 0;
            } else {
                return date.compareTo(comDate);
            }
        } catch (Exception ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * 获取指定时间的longMillis
     *
     * @param time
     * @param fromat
     * @return
     */
    public static long getTimeMillis(String time, String fromat) {
        try {
            SimpleDateFormat sf = new SimpleDateFormat(fromat);
            Date date = sf.parse(time);
            return date.getTime();
        } catch (Exception ex) {
            Logger.getLogger(DateUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     * 获取今天的开始时间
     *
     * @return
     */
    public static Date getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * 判断今天是单号还是双号
     *
     * @return true:双号 false:单号
     */
    public static boolean isDoubleDay() {
        Calendar rightNow = Calendar.getInstance();
        String[] data = rightNow.getTime().toString().split(" ");
        if (Integer.parseInt(data[2]) % 2 == 0) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = df.parse("2019-01-09 00:00:00");
        if (logger.isDebugEnabled()) {
            logger.debug("{}", compareDayToNow(date1));
        }
    }


    /**
     * 时间规则：
     * 1分钟内(刚刚)
     * 1小时内(几分钟前)
     * 一天内(几小时前)
     * 一个月内(几天前)
     * 一年内(MM-dd HH:mm)
     * 超过一年(yyyy-MM-dd)
     */
    public static String getShowTimes(String createTime) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date now = new Date();
            Date date = df.parse(createTime);
            Calendar calendarNow = Calendar.getInstance();
            calendarNow.setTime(now);
            Calendar calendarParam = Calendar.getInstance();
            calendarParam.setTime(date);

            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            if (logger.isDebugEnabled()) {
                logger.debug("------------" + day + "==============" + createTime);
            }
            if (calendarNow.get(Calendar.YEAR) == calendarParam.get(Calendar.YEAR)
                    && calendarNow.get(Calendar.MONTH) == calendarParam.get(Calendar.MONTH)
                    && calendarNow.get(Calendar.DAY_OF_MONTH) == calendarParam.get(Calendar.DAY_OF_MONTH)
                    && hour == 0 && min <= 1) {
                return "刚刚";
            } else if (calendarNow.get(Calendar.YEAR) == calendarParam.get(Calendar.YEAR)
                    && calendarNow.get(Calendar.MONTH) == calendarParam.get(Calendar.MONTH)
                    && calendarNow.get(Calendar.DAY_OF_MONTH) == calendarParam.get(Calendar.DAY_OF_MONTH)
                    && hour < 1) {
                return min + "分钟前";//createTime.substring(11, 16);
            } else if (calendarNow.get(Calendar.YEAR) == calendarParam.get(Calendar.YEAR)
                    && calendarNow.get(Calendar.MONTH) == calendarParam.get(Calendar.MONTH) && day == 0
                    && hour > 1 && hour < 24) {
                return hour + "小时前";// "昨天 " + createTime.substring(11, 16);
            } else if (calendarNow.get(Calendar.YEAR) == calendarParam.get(Calendar.YEAR)
                    && calendarNow.get(Calendar.MONTH) == calendarParam.get(Calendar.MONTH)
                    && day > 0) {
                return day + "天前";// "昨天 " + createTime.substring(11, 16);
            } else if (calendarNow.get(Calendar.YEAR) == calendarParam.get(Calendar.YEAR)) {
                return createTime.substring(5, 16);
            } else {
                return createTime.substring(0, 10);
            }
        } catch (ParseException ex) {
            return createTime;
        }
    }


    /**
     * 将时间戳改成String中的时间戳
     *
     * @param time
     * @return
     */
    public static String changeTimestampToDateString(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }




}
