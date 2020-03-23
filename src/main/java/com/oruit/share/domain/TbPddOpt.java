package com.oruit.share.domain;

import java.io.Serializable;

public class TbPddOpt implements Serializable {
    private Long id;

    private Long optId;

    private String optName;

    private String parentCatId;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOptId() {
        return optId;
    }

    public void setOptId(Long optId) {
        this.optId = optId;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName == null ? null : optName.trim();
    }

    public String getParentCatId() {
        return parentCatId;
    }

    public void setParentCatId(String parentCatId) {
        this.parentCatId = parentCatId == null ? null : parentCatId.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", optId=").append(optId);
        sb.append(", optName=").append(optName);
        sb.append(", parentCatId=").append(parentCatId);
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
        TbPddOpt other = (TbPddOpt) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOptId() == null ? other.getOptId() == null : this.getOptId().equals(other.getOptId()))
            && (this.getOptName() == null ? other.getOptName() == null : this.getOptName().equals(other.getOptName()))
            && (this.getParentCatId() == null ? other.getParentCatId() == null : this.getParentCatId().equals(other.getParentCatId()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOptId() == null) ? 0 : getOptId().hashCode());
        result = prime * result + ((getOptName() == null) ? 0 : getOptName().hashCode());
        result = prime * result + ((getParentCatId() == null) ? 0 : getParentCatId().hashCode());
        return result;
    }
}