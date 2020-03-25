package com.oruit.share.service.impl;

import com.oruit.share.domain.TbUser;
import com.oruit.share.service.WeixinService;
import com.oruit.weixin.WxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
@Service
public class WeixinServiceImpl implements WeixinService {

    @Value("${weixin.appId}")
    private String appId;

    @Value("${weixin.appSecret}")
    private String appSecret;

    @Value("${weixin.token}")
    private String token;

    public TbUser getUser(String code) {
        TbUser user = WxUtils.open_id(code, this.appId, this.appSecret);
        return user;
    }

    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        log.info("signature={},timestamp={},nonce={},token={}", signature, timestamp, nonce, this.token);
        String[] paramArr = { this.token, timestamp, nonce };
        Arrays.sort((Object[])paramArr);
        String content = paramArr[0].concat(paramArr[1]).concat(paramArr[2]);
        String ciphertext = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] digest = md.digest(content.toString().getBytes());
            ciphertext = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return (ciphertext != null) ? ciphertext.equals(signature.toUpperCase()) : false;
    }

    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest = strDigest + byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    private static String byteToHexStr(byte mByte) {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[mByte >>> 4 & 0xF];
        tempArr[1] = Digit[mByte & 0xF];

        String s = new String(tempArr);
        return s;
    }
}
