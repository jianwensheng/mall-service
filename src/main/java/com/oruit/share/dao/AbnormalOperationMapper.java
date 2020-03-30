package com.oruit.share.dao;

import com.oruit.share.domain.AbnormalOperation;

public interface AbnormalOperationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(AbnormalOperation record);

    int insertSelective(AbnormalOperation record);

    AbnormalOperation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AbnormalOperation record);

    int updateByPrimaryKey(AbnormalOperation record);
}