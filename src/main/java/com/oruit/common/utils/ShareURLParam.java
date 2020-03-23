/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oruit.common.utils;

import lombok.Data;

import java.util.Date;

/**
 * 获取分享链接的参数
 *
 * @author hanfeng
 */
@Data
public class ShareURLParam {
    private String ggc;
    private Integer articleid;
    private String fs;
    private String timeline;
    private String appversion;
    private Short source;
    private String sign;
    private String apid;
    private String fullUrl;
    private Date createtime;
    private String ip;
    private String usershareurlid;
    private Integer urlGetMoneyThreshold;

    private String origin;

    private Integer domaingroupid;

}
