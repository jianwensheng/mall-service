package com.oruit.share.service.impl;

import com.oruit.share.dao.TbUserMapper;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public List<TbUser> queryUser(TbUser user) {
        return tbUserMapper.queryUser(user);
    }

    @Override
    public void insertTbUser(TbUser user) {
        tbUserMapper.insertSelective(user);
    }

    @Override
    public int updateTbUser(TbUser user) {
        return tbUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public TbUser queryOpenIdUserInfo(String openId) {
        return tbUserMapper.queryOpenIdUserInfo(openId);
    }
}
