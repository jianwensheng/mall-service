package com.oruit.share.dao;

import com.oruit.share.domain.TbAccount;

public interface TbAccountMapper extends BaseMapper<TbAccount,Long>{
    int deleteByPrimaryKey(Long id);

    int insert(TbAccount record);

    int insertSelective(TbAccount record);

    TbAccount selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbAccount record);

    int updateByPrimaryKey(TbAccount record);
}