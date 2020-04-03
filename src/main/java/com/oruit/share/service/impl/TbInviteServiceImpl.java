package com.oruit.share.service.impl;

import com.oruit.common.constant.LevelType;
import com.oruit.share.dao.TbInviteMapper;
import com.oruit.share.domain.TbInvite;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.TbInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TbInviteServiceImpl extends BaseServiceImpl<TbInvite,Long> implements TbInviteService {

    @Autowired
    private TbInviteMapper tbInviteMapper;

    @Override
    public void setBaseMapper() {
        super.setBaseMapper(tbInviteMapper);
    }

    @Override
    public void initInvite(TbInvite tbInvite, TbUser user) {
        tbInvite.setUserName(user.getUsername());
        tbInvite.setHeadPic(user.getHeadPic());
        tbInvite.setUserId(user.getId());
        tbInvite.setLevel(Short.valueOf(LevelType.USER_V1.getCode()+""));
        tbInvite.setNumber(0);
        tbInvite.setLevelName(LevelType.USER_V1.getDescription());
        tbInvite.setCreateTime(new Date());
        tbInvite.setUpdateTime(new Date());
        tbInviteMapper.insertSelective(tbInvite);
    }
}
