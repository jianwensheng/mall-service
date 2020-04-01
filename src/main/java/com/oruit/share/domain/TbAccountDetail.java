package com.oruit.share.domain;

import java.io.Serializable;
import java.util.Date;

public class TbAccountDetail implements Serializable {
    private Long id;

    private Double amount;

    private Short ieType;

    private Short tradeType;

    private Long relatedId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private String reason;

    private Double amountBeforeTraded;

    private Double amountOfRemaining;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Short getIeType() {
        return ieType;
    }

    public void setIeType(Short ieType) {
        this.ieType = ieType;
    }

    public Short getTradeType() {
        return tradeType;
    }

    public void setTradeType(Short tradeType) {
        this.tradeType = tradeType;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public Double getAmountBeforeTraded() {
        return amountBeforeTraded;
    }

    public void setAmountBeforeTraded(Double amountBeforeTraded) {
        this.amountBeforeTraded = amountBeforeTraded;
    }

    public Double getAmountOfRemaining() {
        return amountOfRemaining;
    }

    public void setAmountOfRemaining(Double amountOfRemaining) {
        this.amountOfRemaining = amountOfRemaining;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", amount=").append(amount);
        sb.append(", ieType=").append(ieType);
        sb.append(", tradeType=").append(tradeType);
        sb.append(", relatedId=").append(relatedId);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", reason=").append(reason);
        sb.append(", amountBeforeTraded=").append(amountBeforeTraded);
        sb.append(", amountOfRemaining=").append(amountOfRemaining);
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
        TbAccountDetail other = (TbAccountDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getAmount() == null ? other.getAmount() == null : this.getAmount().equals(other.getAmount()))
            && (this.getIeType() == null ? other.getIeType() == null : this.getIeType().equals(other.getIeType()))
            && (this.getTradeType() == null ? other.getTradeType() == null : this.getTradeType().equals(other.getTradeType()))
            && (this.getRelatedId() == null ? other.getRelatedId() == null : this.getRelatedId().equals(other.getRelatedId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getReason() == null ? other.getReason() == null : this.getReason().equals(other.getReason()))
            && (this.getAmountBeforeTraded() == null ? other.getAmountBeforeTraded() == null : this.getAmountBeforeTraded().equals(other.getAmountBeforeTraded()))
            && (this.getAmountOfRemaining() == null ? other.getAmountOfRemaining() == null : this.getAmountOfRemaining().equals(other.getAmountOfRemaining()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getAmount() == null) ? 0 : getAmount().hashCode());
        result = prime * result + ((getIeType() == null) ? 0 : getIeType().hashCode());
        result = prime * result + ((getTradeType() == null) ? 0 : getTradeType().hashCode());
        result = prime * result + ((getRelatedId() == null) ? 0 : getRelatedId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getReason() == null) ? 0 : getReason().hashCode());
        result = prime * result + ((getAmountBeforeTraded() == null) ? 0 : getAmountBeforeTraded().hashCode());
        result = prime * result + ((getAmountOfRemaining() == null) ? 0 : getAmountOfRemaining().hashCode());
        return result;
    }
}