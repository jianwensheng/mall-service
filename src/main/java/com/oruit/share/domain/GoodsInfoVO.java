package com.oruit.share.domain;

import lombok.Data;

@Data
public class GoodsInfoVO {

    private String goodsId;

    private String title;

    private String picUrl;

    private String from;

    private String commission;

    private String price;

    private String couponPrice;

    private String orgPrice;

    private Boolean hasCoupon;

    @Override
    public String toString() {
        return "GoodsInfoVO{" +
                "goodsId='" + goodsId + '\'' +
                ", title='" + title + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", from='" + from + '\'' +
                ", commission='" + commission + '\'' +
                ", price='" + price + '\'' +
                ", couponPrice='" + couponPrice + '\'' +
                ", orgPrice='" + orgPrice + '\'' +
                ", hasCoupon=" + hasCoupon +
                '}';
    }
}
