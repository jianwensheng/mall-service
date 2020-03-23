package com.oruit.share.dao;

import com.oruit.share.domain.TbLucky;

import java.util.List;

public interface TbLuckyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbLucky record);

    int insertSelective(TbLucky record);

    TbLucky selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbLucky record);

    int updateByPrimaryKey(TbLucky record);

    List<TbLucky> queryTbLuckyList();
}