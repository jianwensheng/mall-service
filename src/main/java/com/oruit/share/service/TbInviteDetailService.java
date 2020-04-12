package com.oruit.share.service;

import com.oruit.share.domain.TbInviteDetail;

import java.util.List;


public interface TbInviteDetailService extends BaseService<TbInviteDetail,Long> {

    TbInviteDetail queryTbInviteDetailUserId(Long userId,String invitedOpenId);

    TbInviteDetail finishInvite(String unionId, Long userId);

    List<TbInviteDetail> query(TbInviteDetail invite);

}
