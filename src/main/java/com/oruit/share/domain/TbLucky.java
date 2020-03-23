package com.oruit.share.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TbLucky implements Serializable {
    private Integer id;

    private Long goodId;

    private String gtitle;

    private String gdesc;

    private String mainPic;

    private String goodImgs;

    private String detailPic;

    private BigDecimal originalPrice;

    private Integer stock;

    private Integer cliqueNumber;

    private Integer status;

    private BigDecimal cprice;

    private Integer maxPeople;

    private Date openStartTime;

    private Date openEndTime;

    private Integer enabled;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public String getGtitle() {
        return gtitle;
    }

    public void setGtitle(String gtitle) {
        this.gtitle = gtitle == null ? null : gtitle.trim();
    }

    public String getGdesc() {
        return gdesc;
    }

    public void setGdesc(String gdesc) {
        this.gdesc = gdesc == null ? null : gdesc.trim();
    }

    public String getMainPic() {
        return mainPic;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic == null ? null : mainPic.trim();
    }

    public String getGoodImgs() {
        return goodImgs;
    }

    public void setGoodImgs(String goodImgs) {
        this.goodImgs = goodImgs == null ? null : goodImgs.trim();
    }

    public String getDetailPic() {
        return detailPic;
    }

    public void setDetailPic(String detailPic) {
        this.detailPic = detailPic == null ? null : detailPic.trim();
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getCliqueNumber() {
        return cliqueNumber;
    }

    public void setCliqueNumber(Integer cliqueNumber) {
        this.cliqueNumber = cliqueNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getCprice() {
        return cprice;
    }

    public void setCprice(BigDecimal cprice) {
        this.cprice = cprice;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public Date getOpenStartTime() {
        return openStartTime;
    }

    public void setOpenStartTime(Date openStartTime) {
        this.openStartTime = openStartTime;
    }

    public Date getOpenEndTime() {
        return openEndTime;
    }

    public void setOpenEndTime(Date openEndTime) {
        this.openEndTime = openEndTime;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", goodId=").append(goodId);
        sb.append(", gtitle=").append(gtitle);
        sb.append(", gdesc=").append(gdesc);
        sb.append(", mainPic=").append(mainPic);
        sb.append(", goodImgs=").append(goodImgs);
        sb.append(", detailPic=").append(detailPic);
        sb.append(", originalPrice=").append(originalPrice);
        sb.append(", stock=").append(stock);
        sb.append(", cliqueNumber=").append(cliqueNumber);
        sb.append(", status=").append(status);
        sb.append(", cprice=").append(cprice);
        sb.append(", maxPeople=").append(maxPeople);
        sb.append(", openStartTime=").append(openStartTime);
        sb.append(", openEndTime=").append(openEndTime);
        sb.append(", enabled=").append(enabled);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        TbLucky other = (TbLucky) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGoodId() == null ? other.getGoodId() == null : this.getGoodId().equals(other.getGoodId()))
            && (this.getGtitle() == null ? other.getGtitle() == null : this.getGtitle().equals(other.getGtitle()))
            && (this.getGdesc() == null ? other.getGdesc() == null : this.getGdesc().equals(other.getGdesc()))
            && (this.getMainPic() == null ? other.getMainPic() == null : this.getMainPic().equals(other.getMainPic()))
            && (this.getGoodImgs() == null ? other.getGoodImgs() == null : this.getGoodImgs().equals(other.getGoodImgs()))
            && (this.getDetailPic() == null ? other.getDetailPic() == null : this.getDetailPic().equals(other.getDetailPic()))
            && (this.getOriginalPrice() == null ? other.getOriginalPrice() == null : this.getOriginalPrice().equals(other.getOriginalPrice()))
            && (this.getStock() == null ? other.getStock() == null : this.getStock().equals(other.getStock()))
            && (this.getCliqueNumber() == null ? other.getCliqueNumber() == null : this.getCliqueNumber().equals(other.getCliqueNumber()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCprice() == null ? other.getCprice() == null : this.getCprice().equals(other.getCprice()))
            && (this.getMaxPeople() == null ? other.getMaxPeople() == null : this.getMaxPeople().equals(other.getMaxPeople()))
            && (this.getOpenStartTime() == null ? other.getOpenStartTime() == null : this.getOpenStartTime().equals(other.getOpenStartTime()))
            && (this.getOpenEndTime() == null ? other.getOpenEndTime() == null : this.getOpenEndTime().equals(other.getOpenEndTime()))
            && (this.getEnabled() == null ? other.getEnabled() == null : this.getEnabled().equals(other.getEnabled()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGoodId() == null) ? 0 : getGoodId().hashCode());
        result = prime * result + ((getGtitle() == null) ? 0 : getGtitle().hashCode());
        result = prime * result + ((getGdesc() == null) ? 0 : getGdesc().hashCode());
        result = prime * result + ((getMainPic() == null) ? 0 : getMainPic().hashCode());
        result = prime * result + ((getGoodImgs() == null) ? 0 : getGoodImgs().hashCode());
        result = prime * result + ((getDetailPic() == null) ? 0 : getDetailPic().hashCode());
        result = prime * result + ((getOriginalPrice() == null) ? 0 : getOriginalPrice().hashCode());
        result = prime * result + ((getStock() == null) ? 0 : getStock().hashCode());
        result = prime * result + ((getCliqueNumber() == null) ? 0 : getCliqueNumber().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCprice() == null) ? 0 : getCprice().hashCode());
        result = prime * result + ((getMaxPeople() == null) ? 0 : getMaxPeople().hashCode());
        result = prime * result + ((getOpenStartTime() == null) ? 0 : getOpenStartTime().hashCode());
        result = prime * result + ((getOpenEndTime() == null) ? 0 : getOpenEndTime().hashCode());
        result = prime * result + ((getEnabled() == null) ? 0 : getEnabled().hashCode());
        return result;
    }
}