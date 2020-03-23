package com.oruit.share.dao;

import com.oruit.share.domain.BaseException;

public interface BaseExceptionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BaseException record);

    int insertSelective(BaseException record);

    BaseException selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BaseException record);

    int updateByPrimaryKey(BaseException record);
}