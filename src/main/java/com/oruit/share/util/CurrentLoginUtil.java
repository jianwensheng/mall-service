package com.oruit.share.util;

import com.oruit.share.domain.TbUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CurrentLoginUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(CurrentLoginUtil.class);


	public static void saveToThread(HttpServletRequest request, HttpServletResponse response, TbUser tbUser) {
		CurrentThreadValue value = new CurrentThreadValue();
		value.setTbUser(tbUser);
		value.setRequest(request);
		value.setResponse(response);
		CurrentThreadUtil.setThreadValue(value);
	}

	public static TbUser getLoginInfo() {

		CurrentThreadValue value = CurrentThreadUtil.getThreadValue();
		logger.debug("value={}", value);
		if (value != null && value.getTbUser() != null) {
			return value.getTbUser();
		}
		return null;
	}

}
