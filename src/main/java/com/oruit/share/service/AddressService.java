package com.oruit.share.service;

import com.oruit.share.domain.TbAddress;
import okhttp3.Address;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AddressService {

    List<Address> addressList(Long userId);

    int insertSelective(TbAddress record);

    TbAddress queryAddressById(Integer id);

    int updateTbAddress(TbAddress record);

    int updateDeleteTbAddress(TbAddress record);

    int updateStatusTbAddress(Long userId);

    int delTbAddress(Integer id);

}
