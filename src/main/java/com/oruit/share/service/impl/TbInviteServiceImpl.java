package com.oruit.share.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.oruit.common.constant.LevelType;
import com.oruit.share.constant.InviteLevelEnum;
import com.oruit.share.dao.TbInviteMapper;
import com.oruit.share.domain.TbInvite;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.TbInviteService;
import com.oruit.share.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
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

    @Override
    public boolean checkInviteUpdate(TbInvite invite) {
        log.debug("检查用户是否满足升级条件:" + JSONObject.toJSONString(invite));

        InviteLevelEnum inviteLevel = InviteLevelEnum.getInstance(invite.getLevel());
        log.debug("当前等级:" + inviteLevel);
        if (invite.getNumber() >= inviteLevel.getMinNumber() && inviteLevel.getMaxNumber() >= invite.getNumber()) {
            TbInvite tmp = new TbInvite();
            tmp.setId(invite.getId());
            tmp.setUpdateTime(new Date());
            if (inviteLevel == InviteLevelEnum.JUNIOR) {
                log.debug("初级升中级");
                tmp.setLevel(InviteLevelEnum.MIDDLE.getCode());
                tmp.setLevelName(InviteLevelEnum.MIDDLE.getName());
            } else if (inviteLevel == InviteLevelEnum.MIDDLE) {
                log.debug("中级升高级");
                tmp.setLevel(InviteLevelEnum.SENIOR.getCode());
                tmp.setLevelName(InviteLevelEnum.SENIOR.getName());
            }
            this.updateByPrimaryKeySelective(tmp);
        }

        return false;
    }

    @Override
    public int addInviteNumber(Long userId) {
        return tbInviteMapper.addInviteNumber(userId);
    }

    @Override
    public TbInvite getByUserId(Long userId) {
        TbInvite param = new TbInvite();
        param.setUserId(userId);
        List<TbInvite> inviteList = tbInviteMapper.query(param);
        if (!CommonUtil.isEmptyList(inviteList)) {
            return inviteList.get(0);
        }
        return null;
    }
}
