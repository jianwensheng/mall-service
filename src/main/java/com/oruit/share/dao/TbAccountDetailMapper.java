package com.oruit.share.dao;

import com.oruit.share.domain.TbAccountDetail;

public interface TbAccountDetailMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbAccountDetail record);

    int insertSelective(TbAccountDetail record);

    TbAccountDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbAccountDetail record);

    int updateByPrimaryKey(TbAccountDetail record);
}