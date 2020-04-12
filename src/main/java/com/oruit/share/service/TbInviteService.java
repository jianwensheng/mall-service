package com.oruit.share.service;


import com.oruit.share.domain.TbInvite;
import com.oruit.share.domain.TbUser;

public interface TbInviteService extends BaseService<TbInvite,Long> {

    void initInvite(TbInvite tbInvite, TbUser user);

    boolean checkInviteUpdate(TbInvite invite);

    int addInviteNumber(Long userId);

    TbInvite getByUserId(Long userId);

}
