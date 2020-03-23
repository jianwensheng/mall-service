package com.oruit.share.service.impl;

import com.oruit.share.dao.TbAddressMapper;
import com.oruit.share.domain.TbAddress;
import com.oruit.share.service.AddressService;
import okhttp3.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private TbAddressMapper tbAddressMapper;

    @Override
    public List<Address> addressList(Long userId) {
        return tbAddressMapper.addressList(userId);
    }

    @Override
    public int insertSelective(TbAddress record) {
        return tbAddressMapper.insertSelective(record);
    }

    @Override
    public TbAddress queryAddressById(Integer id) {
        return tbAddressMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateTbAddress(TbAddress record) {
        return tbAddressMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateDeleteTbAddress(TbAddress record) {
        return tbAddressMapper.updateDeleteTbAddress(record);
    }


}
