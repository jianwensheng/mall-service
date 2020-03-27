package com.oruit.share.service;

public interface WeixinService {

     boolean checkSignature(String signature, String timestamp, String nonce);

}
