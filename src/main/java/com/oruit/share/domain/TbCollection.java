package com.oruit.share.domain;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TbCollection implements Serializable {
    private Long id;

    private Long userId;

    private Long goodId;

    private String plat;

    private Integer status;

    private String goodName;

    private String goodDesc;

    private String goodImg;

    private String couponPrice;

    private BigDecimal actualPrice;

    private BigDecimal originalPrice;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;


}