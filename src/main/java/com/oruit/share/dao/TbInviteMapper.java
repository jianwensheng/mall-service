package com.oruit.share.dao;

import com.oruit.share.domain.TbInvite;

public interface TbInviteMapper extends BaseMapper<TbInvite,Long>{
    int deleteByPrimaryKey(Long id);

    int insert(TbInvite record);

    int insertSelective(TbInvite record);

    TbInvite selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbInvite record);

    int updateByPrimaryKey(TbInvite record);
}