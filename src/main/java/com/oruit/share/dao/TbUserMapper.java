package com.oruit.share.dao;

import com.oruit.share.domain.TbUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TbUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbUser record);

    int insertSelective(TbUser record);

    TbUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);

    List<TbUser> queryUser(TbUser user);

    TbUser queryOpenIdUserInfo(String openId);

    TbUser queryuserTokenUserInfo(@Param("userToken") String userToken);
}