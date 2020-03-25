package com.oruit.share.service;

import com.oruit.share.domain.TbUser;

public interface WeixinService {

    public TbUser getUser(String code);

    public boolean checkSignature(String signature, String timestamp, String nonce);

}
