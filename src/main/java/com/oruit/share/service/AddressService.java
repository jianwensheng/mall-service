package com.oruit.share.service;

import com.oruit.share.domain.TbAddress;
import okhttp3.Address;

import java.util.List;

public interface AddressService {

    List<Address> addressList(Long userId);

    int insertSelective(TbAddress record);

    TbAddress queryAddressById(Integer id);

    int updateTbAddress(TbAddress record);

    int updateDeleteTbAddress(TbAddress record);
}
