package com.oruit.share.dao;

import com.oruit.share.domain.TbCollection;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface TbCollectionMapper extends BaseMapper<TbCollection,Long>{
    int deleteByPrimaryKey(Long id);

    int insert(TbCollection record);

    int insertSelective(TbCollection record);

    TbCollection selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbCollection record);

    int updateByPrimaryKey(TbCollection record);

    List<TbCollection> queryTbCollectionList(HashMap map);

    TbCollection queryTbCollection(@Param("userId") Long userId,@Param("goodId") Long goodsId);
}