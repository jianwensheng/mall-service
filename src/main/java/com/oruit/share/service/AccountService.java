package com.oruit.share.service;

import com.oruit.share.domain.TbAccount;

public interface AccountService extends BaseService<TbAccount,Long> {

    TbAccount queryTbAccountInfo(Long userId);
}
