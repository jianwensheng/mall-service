package com.oruit.share.dao;

import com.oruit.share.domain.TbInviteDetail;

public interface TbInviteDetailMapper extends BaseMapper<TbInviteDetail,Long>{
    int deleteByPrimaryKey(Long id);

    int insert(TbInviteDetail record);

    int insertSelective(TbInviteDetail record);

    TbInviteDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbInviteDetail record);

    int updateByPrimaryKey(TbInviteDetail record);
}