package com.oruit.share.dao;

import com.oruit.share.domain.TbInviteDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbInviteDetailMapper extends BaseMapper<TbInviteDetail,Long>{
    int deleteByPrimaryKey(Long id);

    int insert(TbInviteDetail record);

    int insertSelective(TbInviteDetail record);

    TbInviteDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbInviteDetail record);

    int updateByPrimaryKey(TbInviteDetail record);

    TbInviteDetail queryTbInviteDetailUserId(@Param("userId") Long userId, @Param("invitedOpenId") String invitedOpenId);

    List<TbInviteDetail> query(TbInviteDetail invite);
}