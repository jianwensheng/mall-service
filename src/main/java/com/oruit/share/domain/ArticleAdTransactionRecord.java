package com.oruit.share.domain;

import java.math.BigDecimal;
import java.util.Date;

public class ArticleAdTransactionRecord {
    private Integer id;

    private Integer articleId;

    private String ggc;

    private Integer adType;

    private Integer mediaArticleId;

    private String adUrl;

    private Date createTime;

    private String advertiser;

    private BigDecimal payAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getGgc() {
        return ggc;
    }

    public void setGgc(String ggc) {
        this.ggc = ggc == null ? null : ggc.trim();
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }

    public Integer getMediaArticleId() {
        return mediaArticleId;
    }

    public void setMediaArticleId(Integer mediaArticleId) {
        this.mediaArticleId = mediaArticleId;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl == null ? null : adUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAdvertiser() {
        return advertiser;
    }

    public void setAdvertiser(String advertiser) {
        this.advertiser = advertiser == null ? null : advertiser.trim();
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }
}