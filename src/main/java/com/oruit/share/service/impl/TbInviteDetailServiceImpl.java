package com.oruit.share.service.impl;

import com.oruit.share.dao.TbInviteDetailMapper;
import com.oruit.share.domain.TbInviteDetail;
import com.oruit.share.service.TbInviteDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TbInviteDetailServiceImpl extends BaseServiceImpl<TbInviteDetail,Long> implements TbInviteDetailService {

    @Autowired
    private TbInviteDetailMapper tbInviteDetailMapper;

    @Override
    public void setBaseMapper() {
        super.setBaseMapper(tbInviteDetailMapper);
    }
}
