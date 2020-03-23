package com.oruit.share.domain;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TbBrand implements Serializable {
    private Long id;

    private String brandId;

    private String brandName;

    private String brandLogo;

    private String brandEnglish;

    private String name;

    private Integer sellerId;

    private BigDecimal brandScore;

    private String location;

    private String establishTime;

    private String belong;

    private String position;

    private String consumer;

    private String label;

    private String simpleLabel;

    private String cids;

    private JSONArray goodsList;

    private static final long serialVersionUID = 1L;

}