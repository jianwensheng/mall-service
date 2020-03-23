package com.oruit.share.dao;

import com.oruit.share.domain.TbPddOpt;

import java.util.List;

public interface TbPddOptMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbPddOpt record);

    int insertSelective(TbPddOpt record);

    TbPddOpt selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbPddOpt record);

    int updateByPrimaryKey(TbPddOpt record);

    List<TbPddOpt> queryTbPddOptList();
}