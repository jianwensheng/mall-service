package com.oruit.share.service;

import com.oruit.share.domain.TbUser;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {

    List<TbUser> queryUser(TbUser user);

    void insertTbUser(TbUser user);

    int updateTbUser(TbUser user);

    TbUser queryOpenIdUserInfo(String openId);

    TbUser queryTokenUserInfo(String userToken);

    TbUser generateTokenAndSave(TbUser userInfo, HttpSession session);
}
