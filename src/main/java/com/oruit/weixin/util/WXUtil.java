package com.oruit.weixin.util;

import com.alibaba.fastjson.JSONObject;
import com.oruit.share.domain.AccessToken;
import com.oruit.share.domain.TbUser;
import com.oruit.share.util.ApplicationContextUtil;
import com.oruit.util.SimpleRequestUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WXUtil {

	static Logger payLog = LoggerFactory.getLogger("payLog");
	static Logger businessLog = LoggerFactory.getLogger("businessLog");

	static Boolean isTest;

	public static String getLoginAppId() { 
		return ApplicationContextUtil.getBean(Environment.class).getProperty("login.wx.appId", "wx3f89d2c7ededa0d9");
	}

	public static String getLoginAppSecret() {
 		return ApplicationContextUtil.getBean(Environment.class).getProperty("login.wx.appSecret", "417fb5ac86136846e07d7697fa0aba09");
	}

	public static String getPayAppId() {
		return ApplicationContextUtil.getBean(Environment.class).getProperty("pay.wx.appId", "wx474c64e0017cec51");
	}

	public static String getPayAppSecret() {
		return ApplicationContextUtil.getBean(Environment.class).getProperty("pay.wx.appSecret", "de13b02ad163854b49fa472610b168bc");
	}

	public static String getMchAppId() {
		return ApplicationContextUtil.getBean(Environment.class).getProperty("pay.wx.mchAppId", "wx3f89d2c7ededa0d9");
	}

	public static String getPayMchId() {
		return ApplicationContextUtil.getBean(Environment.class).getProperty("pay.wx.mchId", "1521637671");
	}

	public static String getPayKey() {
		return ApplicationContextUtil.getBean(Environment.class).getProperty("pay.wx.key", "de13b02ad163854b49fa472610b168bc");
	}

	public static String getInviteAppId() {
		return ApplicationContextUtil.getBean(Environment.class).getProperty("invite.wx.appId", "wx3f89d2c7ededa0d9");
	}

	public static String getInviteAppSecret() {
		return ApplicationContextUtil.getBean(Environment.class).getProperty("invite.wx.appSecret", "e5269a5e811d530b641e68c65f3fa69a");
	}

	private static boolean isTest() {
		if (isTest == null) {
			isTest = ApplicationContextUtil.getBean(Environment.class).getProperty("isTest", Boolean.class, true);
		}
		return isTest;
	}

	public static AccessToken getAccessToken(String code) throws IOException {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + getLoginAppId() + "&secret=" + getLoginAppSecret() + "&code=" + code
				+ "&grant_type=authorization_code";

		// https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code

		// https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
		String result = SimpleRequestUtil.getResponseText(url);
		System.out.println("getAccessToken="+result+",url="+url);
		AccessToken accessToken = null;
		try {
			accessToken = JSONObject.parseObject(result, AccessToken.class);
		} catch (RuntimeException e) {
			accessToken = null;
			e.printStackTrace();
		}
		return accessToken;
	}

	public static AccessToken getInviteAccessToken(String code) throws IOException {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + getInviteAppId() + "&secret=" + getInviteAppSecret() + "&code=" + code
				+ "&grant_type=authorization_code";

		// https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code

		// https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140842
		String result = SimpleRequestUtil.getResponseText(url);
		AccessToken accessToken = null;
		try {
			accessToken = JSONObject.parseObject(result, AccessToken.class);
			businessLog.info("getInviteAccessToken.result:" + JSONObject.toJSONString(accessToken));
		} catch (RuntimeException e) {
			accessToken = null;
			e.printStackTrace();
		}
		return accessToken;
	}

	public static TbUser getUserInfo(String accessToken, String openId) throws IOException {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
		String result = SimpleRequestUtil.getResponseText(url);
		TbUser userInfo = null;
		try {
			userInfo = JSONObject.parseObject(result, TbUser.class);
			if(!StringUtils.isEmpty(userInfo.getHeadPic())) {
				userInfo.setHeadPic(userInfo.getHeadPic().replace("http", "https"));
			} 
		} catch (RuntimeException e) {
			userInfo = null;
			e.printStackTrace();
		}
		return userInfo;
	}

//	public static WXOrderResult unifiedorder(WXOrder wxOrder) throws ClientProtocolException, IOException {
//		/*if (isTest()) {
//			WXOrderResult orderResult = new WXOrderResult();
//			orderResult.setAppId(wxOrder.getAppId());
//			orderResult.setCreateTime(new Date());
//			orderResult.setMchId(wxOrder.getMchId());
//			orderResult.setNonceStr("sssssss");
//			orderResult.setNotifyUrl("http://localhost:8099/dida/wx/notify");
//			orderResult.setOutTradeNo(wxOrder.getOutTradeNo());
//			orderResult.setPrepayId(UUID.randomUUID().toString());
//			orderResult.setResultCode(DiandiConstants.SUCCESS());
//			orderResult.setReturnCode(DiandiConstants.SUCCESS());
//			orderResult.setSign(wxOrder.getSign());
//			orderResult.setTotalFee(wxOrder.getTotalFee());
//			orderResult.setUserId(CurrentLoginUtil.getLoginInfo().getUserId());
//			return orderResult;
//		}*/
//
//		HttpClient httpClient = HttpClientBuilder.create().build();
//
//		String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//		HttpPost httpPost = new HttpPost(url);
//
//		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
//		httpPost.setConfig(requestConfig);
//
//		String xmlParam = generateXmlParam(wxOrder);
//		payLog.info("微信支付xml参数：" + xmlParam);
//		StringEntity postEntity = new StringEntity(xmlParam, "UTF-8");
//		httpPost.addHeader("Content-Type", "text/xml");
//		httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + wxOrder.getMchId()); // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
//		httpPost.setEntity(postEntity);
//
//		HttpResponse httpResponse = httpClient.execute(httpPost);
//		HttpEntity httpEntity = httpResponse.getEntity();
//		String str = EntityUtils.toString(httpEntity, "UTF-8");
//		payLog.info("微信下单结果:" + str);
//		WXOrderResult orderResult = JSONObject.parseObject(JsonXmlUtil.parseXml2Json(str).toJSONString(), WXOrderResult.class);
//		return orderResult;
//	}


	/*public static WXOrderResult transfers(WXTransfers wxTransfers) throws IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		String url = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";
		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		httpPost.setConfig(requestConfig);

		String xmlParam = generateXmlParam(wxTransfers);
		payLog.info("微信企业付款xml参数：" + xmlParam);
		StringEntity postEntity = new StringEntity(xmlParam, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + wxTransfers.getMchId()); // TODO: 很重要，用来检测 sdk 的使用情况，要不要加上商户信息？
		httpPost.setEntity(postEntity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();
		String str = EntityUtils.toString(httpEntity, "UTF-8");
		payLog.info("微信企业付款结果:" + str);
		WXOrderResult orderResult = JSONObject.parseObject(JsonXmlUtil.parseXml2Json(str).toJSONString(), WXOrderResult.class);
		return orderResult;
	}*/


/*	public static String notify(WXNotifyResult wxNotifyResult) {
		if (wxNotifyResult.getResultCode().equals(DiandiConstants.SUCCESS())) {
			WXNotifyResult result = new WXNotifyResult();
			result.setReturnCode(DiandiConstants.SUCCESS());
			result.setReturnMsg("支付成功");
			return JsonXmlUtil.parstJson2Xml(JSONObject.parseObject(JSONObject.toJSONString(result)));
		}

		return null;
	} */

	public static String getTimeByWXFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}

	/*public static String generateSign(WXOrder order) {
		return generateSign(JSONObject.parseObject(JSONObject.toJSONString(order)));
	}*/

	@SuppressWarnings({ "unchecked", "rawtypes" })
	/*public static String generateSign(JSONObject json) {
		Map<String, String> map = JSONObject.parseObject(json.toJSONString(), Map.class);
		SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);
		StringBuffer sb = new StringBuffer();
		Set<Entry<String, String>> es = sortedMap.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String k = (String) entry.getKey();
			Object v = entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + getPayKey());
		String sign = MD5Util.MD5Encode(sb.toString(), "utf-8").toUpperCase();
		payLog.info("微信支付签名sign:" + sign);
		return sign;
	}*/

	/*public static String generateXmlParam(WXOrder order) {
		Map<String, String> map = JSONObject.parseObject(JSONObject.toJSONString(order), Map.class);
		SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);
		StringBuffer xmlStr = new StringBuffer("<xml>");
		Set<Entry<String, String>> es = sortedMap.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
				xmlStr.append("<" + key + ">").append(JsonXmlUtil.setNodeValue(value.toString())).append("</" + key + ">");
			}
		}
		xmlStr.append("<sign>").append(JsonXmlUtil.setNodeValue(order.getSign())).append("</sign>");
		xmlStr.append("</xml>");
		payLog.info("微信支付xmlParam:" + xmlStr.toString());
		return xmlStr.toString();
	}*/

	/*public static String generateXmlParam(WXTransfers wxTransfers) {
		Map<String, String> map = JSONObject.parseObject(JSONObject.toJSONString(wxTransfers), Map.class);
		SortedMap<String, String> sortedMap = new TreeMap<String, String>(map);
		StringBuffer xmlStr = new StringBuffer("<xml>");
		Set<Entry<String, String>> es = sortedMap.entrySet();// 所有参与传参的参数按照accsii排序（升序）
		Iterator<Entry<String, String>> it = es.iterator();
		while (it.hasNext()) {
			Entry entry = (Entry) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
				xmlStr.append("<" + key + ">").append(JsonXmlUtil.setNodeValue(value.toString())).append("</" + key + ">");
			}
		}
		xmlStr.append("<sign>").append(JsonXmlUtil.setNodeValue(wxTransfers.getSign())).append("</sign>");
		xmlStr.append("</xml>");
		payLog.info("微信支付xmlParam:" + xmlStr.toString());
		return xmlStr.toString();
	}*/

	public static void main(String[] args) {
		// String tradeNo =
		// "appid=wx0c6491c3a308cd06&body=diandiUser:吃螃蟹发红包，总金额：0.2&mch_id=1502508781&nonce_str=15242998930819615&notify_url=http://www.dida.com/dida/wx/notify&out_trade_no=daa46b248e384362a752b523d0aa0a2f&spbill_create_ip=127.0.0.1&time_start=20180421163813&total_fee=20&trade_type=APP&key=cc6a79a5ab60a31b420b3af9a929285d";
		// String sign = MD5Util.MD5Encode(tradeNo, "utf-8").toUpperCase();
		// payLog.info(sign);

		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
	}

}
