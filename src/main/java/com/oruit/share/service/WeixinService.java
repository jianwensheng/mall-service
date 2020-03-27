package com.oruit.share.service;

import com.oruit.share.domain.TbUser;

public interface WeixinService {

     TbUser getUser(String code);

     boolean checkSignature(String signature, String timestamp, String nonce);

}
