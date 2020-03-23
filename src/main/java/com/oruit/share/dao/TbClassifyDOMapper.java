package com.oruit.share.dao;

import com.oruit.share.domain.TbClassifyDO;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TbClassifyDOMapper {

    int deleteByPrimaryKey(Integer classifyId);

    int insert(TbClassifyDO record);

    int insertSelective(TbClassifyDO record);

    TbClassifyDO selectByPrimaryKey(Integer classifyId);

    int updateByPrimaryKeySelective(TbClassifyDO record);

    int updateByPrimaryKey(TbClassifyDO record);

    List<TbClassifyDO> queryTbClassify(Integer levelId);
}