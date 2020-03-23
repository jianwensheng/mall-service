package com.oruit.share.dao;

import com.oruit.share.domain.TbPddCat;


public interface TbPddCatMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbPddCat record);

    int insertSelective(TbPddCat record);

    TbPddCat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbPddCat record);

    int updateByPrimaryKey(TbPddCat record);

}