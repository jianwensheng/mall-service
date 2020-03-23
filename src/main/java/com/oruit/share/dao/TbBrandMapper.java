package com.oruit.share.dao;

import com.oruit.share.domain.TbBrand;

import java.util.HashMap;
import java.util.List;

public interface TbBrandMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbBrand record);

    int insertSelective(TbBrand record);

    TbBrand selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbBrand record);

    int updateByPrimaryKey(TbBrand record);

    List<TbBrand> queryTbBrandList(HashMap map);

    List<TbBrand> queryTbBrandTopList();

    TbBrand selectByBrandId(Long brandId);
}