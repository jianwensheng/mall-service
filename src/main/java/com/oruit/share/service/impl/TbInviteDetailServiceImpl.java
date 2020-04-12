package com.oruit.share.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.oruit.share.dao.TbInviteDetailMapper;
import com.oruit.share.domain.TbInvite;
import com.oruit.share.domain.TbInviteDetail;
import com.oruit.share.service.TbInviteDetailService;
import com.oruit.share.service.TbInviteService;
import com.oruit.share.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class TbInviteDetailServiceImpl extends BaseServiceImpl<TbInviteDetail,Long> implements TbInviteDetailService {

    @Autowired
    private TbInviteDetailMapper tbInviteDetailMapper;

    @Autowired
    private TbInviteService tbInviteService;

    @Override
    public void setBaseMapper() {
        super.setBaseMapper(tbInviteDetailMapper);
    }

    @Override
    public TbInviteDetail queryTbInviteDetailUserId(Long userId, String invitedOpenId) {
        return tbInviteDetailMapper.queryTbInviteDetailUserId(userId,invitedOpenId);
    }

    @Override
    public List<TbInviteDetail> query(TbInviteDetail invite) {
        return tbInviteDetailMapper.query(invite);
    }

    @Override
    public TbInviteDetail finishInvite(String unionId, Long invitedUserId) {
            log.info("完成邀请流程");
            log.info("unionId:" + unionId + ";invitedUserId:" + invitedUserId);
            TbInviteDetail param = new TbInviteDetail();
            param.setInvitedUnionid(unionId);
            List<TbInviteDetail> list = this.query(param);
            if (!CommonUtil.isEmptyList(list)) {
                log.info("原始邀请信息:" + JSONObject.toJSONString(list));
                TbInviteDetail detail = list.get(0);
                detail.setUpdateTime(new Date());
                detail.setInvitedUserId(invitedUserId);
                this.updateByPrimaryKeySelective(detail);
                tbInviteService.addInviteNumber(detail.getUserId());
                TbInvite invite = tbInviteService.getByUserId(detail.getUserId());
                tbInviteService.checkInviteUpdate(invite);
                return detail;
            }
            log.warn("原始邀请信息为空");
            return null;
    }

}
