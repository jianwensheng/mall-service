package com.oruit.share.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TbAccount implements Serializable {
    private Long id;

    private BigDecimal withdrawAmount;

    private BigDecimal cashAmount;

    private Integer waitSettleCount;

    private BigDecimal waitCmisAmount;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public BigDecimal getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(BigDecimal cashAmount) {
        this.cashAmount = cashAmount;
    }

    public Integer getWaitSettleCount() {
        return waitSettleCount;
    }

    public void setWaitSettleCount(Integer waitSettleCount) {
        this.waitSettleCount = waitSettleCount;
    }

    public BigDecimal getWaitCmisAmount() {
        return waitCmisAmount;
    }

    public void setWaitCmisAmount(BigDecimal waitCmisAmount) {
        this.waitCmisAmount = waitCmisAmount;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", withdrawAmount=").append(withdrawAmount);
        sb.append(", cashAmount=").append(cashAmount);
        sb.append(", waitSettleCount=").append(waitSettleCount);
        sb.append(", waitCmisAmount=").append(waitCmisAmount);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
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
        TbAccount other = (TbAccount) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getWithdrawAmount() == null ? other.getWithdrawAmount() == null : this.getWithdrawAmount().equals(other.getWithdrawAmount()))
            && (this.getCashAmount() == null ? other.getCashAmount() == null : this.getCashAmount().equals(other.getCashAmount()))
            && (this.getWaitSettleCount() == null ? other.getWaitSettleCount() == null : this.getWaitSettleCount().equals(other.getWaitSettleCount()))
            && (this.getWaitCmisAmount() == null ? other.getWaitCmisAmount() == null : this.getWaitCmisAmount().equals(other.getWaitCmisAmount()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getWithdrawAmount() == null) ? 0 : getWithdrawAmount().hashCode());
        result = prime * result + ((getCashAmount() == null) ? 0 : getCashAmount().hashCode());
        result = prime * result + ((getWaitSettleCount() == null) ? 0 : getWaitSettleCount().hashCode());
        result = prime * result + ((getWaitCmisAmount() == null) ? 0 : getWaitCmisAmount().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }
}