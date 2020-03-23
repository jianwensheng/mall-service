package com.oruit.share.dao;

import com.oruit.share.domain.TbBannerDO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TbBannerDOMapper {

    int deleteByPrimaryKey(Integer bannerId);

    int insert(TbBannerDO record);

    int insertSelective(TbBannerDO record);

    TbBannerDO selectByPrimaryKey(Integer bannerId);

    int updateByPrimaryKeySelective(TbBannerDO record);

    int updateByPrimaryKey(TbBannerDO record);

    List<TbBannerDO> queryTbBanner(Map map);
}