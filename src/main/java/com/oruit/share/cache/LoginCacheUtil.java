package com.oruit.share.cache;


import com.oruit.common.utils.cache.redis.RedisUtil;
import com.oruit.share.constant.RedisConstant;
import com.oruit.share.domain.AbnormalOperation;
import com.oruit.share.domain.TbUser;
import com.oruit.share.service.AbnormalOperationService;
import com.oruit.share.service.UserService;
import com.oruit.share.util.ApplicationContextUtil;

public class LoginCacheUtil {

	public static void remove(String userToken) {
		RedisUtil.remove(RedisConstant.USER_LOGIN_INFO_KEY + userToken);
	}

	public static boolean save(TbUser tbUser) {
		RedisUtil.setObject(RedisConstant.USER_LOGIN_INFO_KEY + tbUser.getToken()
				,RedisConstant.HALF_MONTH,tbUser);
		return true;

	}

	public static TbUser get(String userToken) {
		TbUser login = RedisUtil.getObject(RedisConstant.USER_LOGIN_INFO_KEY + userToken, TbUser.class);
		if (login == null) {
			UserService diandiLoginService = ApplicationContextUtil.getBean(UserService.class);
			login = diandiLoginService.queryTokenUserInfo(userToken);
			if (login != null) {
				RedisUtil.setObject(RedisConstant.USER_LOGIN_INFO_KEY + userToken, RedisConstant.HALF_MONTH,login);
			}
		}
		return login;
	}

	public static TbUser getOpenInfo(String openId) {
		TbUser login = RedisUtil.getObject(RedisConstant.USER_LOGIN_OPEN_INFO_KEY + openId, TbUser.class);
		if (login == null) {
			UserService diandiLoginService = ApplicationContextUtil.getBean(UserService.class);
			login = diandiLoginService.queryOpenIdUserInfo(openId);
			if (login != null) {
				RedisUtil.setObject(RedisConstant.USER_LOGIN_OPEN_INFO_KEY+openId,RedisConstant.HALF_MONTH,login);
			}
		}
		return login;
	}
	
	public static void saveAOper(AbnormalOperation abnormalOperation) {
		    AbnormalOperationService abnormalOperationService = ApplicationContextUtil.getBean(AbnormalOperationService.class);
		    abnormalOperationService.insertSelective(abnormalOperation);
	}
	
	public static void main(String[] args) {
		RedisUtil.remove(RedisConstant.USER_LOGIN_INFO_KEY+"c9ee7d95-fbf3-4dc8-8cfe-c95f12b2e9d6");
	}
}
