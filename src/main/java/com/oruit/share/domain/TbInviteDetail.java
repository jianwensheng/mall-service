package com.oruit.share.domain;

import java.io.Serializable;
import java.util.Date;

public class TbInviteDetail implements Serializable {
    private Long id;

    private Long invitedUserId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private String invitedOpenId;

    private String invitedUnionid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvitedUserId() {
        return invitedUserId;
    }

    public void setInvitedUserId(Long invitedUserId) {
        this.invitedUserId = invitedUserId;
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

    public String getInvitedOpenId() {
        return invitedOpenId;
    }

    public void setInvitedOpenId(String invitedOpenId) {
        this.invitedOpenId = invitedOpenId == null ? null : invitedOpenId.trim();
    }

    public String getInvitedUnionid() {
        return invitedUnionid;
    }

    public void setInvitedUnionid(String invitedUnionid) {
        this.invitedUnionid = invitedUnionid == null ? null : invitedUnionid.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", invitedUserId=").append(invitedUserId);
        sb.append(", userId=").append(userId);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", invitedOpenId=").append(invitedOpenId);
        sb.append(", invitedUnionid=").append(invitedUnionid);
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
        TbInviteDetail other = (TbInviteDetail) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getInvitedUserId() == null ? other.getInvitedUserId() == null : this.getInvitedUserId().equals(other.getInvitedUserId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getInvitedOpenId() == null ? other.getInvitedOpenId() == null : this.getInvitedOpenId().equals(other.getInvitedOpenId()))
            && (this.getInvitedUnionid() == null ? other.getInvitedUnionid() == null : this.getInvitedUnionid().equals(other.getInvitedUnionid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getInvitedUserId() == null) ? 0 : getInvitedUserId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getInvitedOpenId() == null) ? 0 : getInvitedOpenId().hashCode());
        result = prime * result + ((getInvitedUnionid() == null) ? 0 : getInvitedUnionid().hashCode());
        return result;
    }
}