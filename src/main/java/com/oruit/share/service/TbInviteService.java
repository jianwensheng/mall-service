package com.oruit.share.service;


import com.oruit.share.domain.TbInvite;
import com.oruit.share.domain.TbUser;

public interface TbInviteService extends BaseService<TbInvite,Long> {

    void initInvite(TbInvite tbInvite, TbUser user);
}
