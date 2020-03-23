package com.oruit.share.dao;

import com.oruit.share.domain.TbAddress;
import okhttp3.Address;

import java.util.List;

public interface TbAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TbAddress record);

    int insertSelective(TbAddress record);

    TbAddress selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TbAddress record);

    int updateByPrimaryKey(TbAddress record);

    List<Address> addressList(Long userId);

    int updateDeleteTbAddress(TbAddress record);
}