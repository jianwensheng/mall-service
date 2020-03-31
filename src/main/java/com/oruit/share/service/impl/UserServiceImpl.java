package com.oruit.share.service.impl;

import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.share.cache.LoginCacheUtil;
import com.oruit.share.constant.RedisConstant;
import com.oruit.share.dao.TbUserMapper;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.UserService;
import com.oruit.share.util.ApplicationContextUtil;
import com.oruit.weixin.EmojiFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Override
    public List<TbUser> queryUser(TbUser user) {
        return tbUserMapper.queryUser(user);
    }

    @Override
    public void insertTbUser(TbUser user) {
        tbUserMapper.insertSelective(user);
    }

    @Override
    public int updateTbUser(TbUser user) {
        return tbUserMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public TbUser queryOpenIdUserInfo(String openId) {
        TbUser login = RedisUtil.getObject(RedisConstant.USER_LOGIN_OPEN_INFO_KEY + openId, TbUser.class);
        if (login == null) {
            login = tbUserMapper.queryOpenIdUserInfo(openId);
            if (login != null) {
                RedisUtil.setObject(RedisConstant.USER_LOGIN_OPEN_INFO_KEY+openId,RedisConstant.HALF_HOUR,login);
            }
        }
        return login;
    }

    @Override
    public TbUser queryTokenUserInfo(String userToken) {
        TbUser login = RedisUtil.getObject(RedisConstant.USER_LOGIN_INFO_KEY + userToken, TbUser.class);
        if (login == null) {
            login = tbUserMapper.queryuserTokenUserInfo(userToken);
            if (login != null) {
                RedisUtil.setObject(RedisConstant.USER_LOGIN_INFO_KEY + userToken, RedisConstant.HALF_HOUR,login);
            }
        }
        return login;
    }

    @Override
    public TbUser generateTokenAndSave(TbUser userInfo, HttpSession session) {
        String userToken = UUID.randomUUID().toString().replaceAll("-", "");
        TbUser tbUser = new TbUser();
        tbUser.setOpenId(userInfo.getOpenId());
        tbUser.setCode(userInfo.getCode());
        tbUser.setToken(userToken);
        tbUser.setUsername(EmojiFilter.filterEmoji(String.valueOf(userInfo.getUsername())));
        tbUser.setSex(Short.valueOf(userInfo.getSex().toString()));
        tbUser.setCountry(String.valueOf(userInfo.getCountry()));
        tbUser.setProvince(String.valueOf(userInfo.getProvince()));
        tbUser.setCity(String.valueOf(userInfo.getCity()));
        tbUser.setLanguage(String.valueOf(userInfo.getLanguage()));
        tbUser.setHeadPic(String.valueOf(userInfo.getHeadPic()));
        TbUser user = queryOpenIdUserInfo(userInfo.getOpenId());
        if (user == null) {
            user = new TbUser();
            user.setUsername(userInfo.getUsername());
            user.setCreateTime(new Date());
            user.setOpenId(userInfo.getOpenId());
            user.setUpdateTime(new Date());
            user.setUnionId(userInfo.getUnionId());
            user.setHeadPic(userInfo.getHeadPic());
            user.setStatus((short) 1);
            user.setToken(userToken);
            insertTbUser(user);
            //accountService.init(user.getId());
            //inviteDetailService.finishInvite(userInfo.getUnionId(), user.getId());
        }else {
            updateTbUser(user);
        }
        if (user.getStatus() == null || user.getStatus().shortValue() != 1) {
            return null;
        }
        session.setAttribute("openId",userInfo.getOpenId());
        LoginCacheUtil.save(user);
        return user;
    }
}
