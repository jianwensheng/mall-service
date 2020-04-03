package com.oruit.share.service.impl;

import com.oruit.share.dao.TbAccountMapper;
import com.oruit.share.domain.TbAccount;
import com.oruit.share.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceImpl extends BaseServiceImpl<TbAccount,Long> implements AccountService{

    @Autowired
    private TbAccountMapper tbAccountMapper;

    @Override
    public void setBaseMapper() {
        super.setBaseMapper(tbAccountMapper);
    }

}
