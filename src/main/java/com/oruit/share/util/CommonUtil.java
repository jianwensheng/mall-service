package com.oruit.share.util;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CommonUtil {

	static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	public static String md5(String input) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			logger.error("获取md5对象失败", e);
		}
		String s = input; // 将要加密的字符串
		byte[] bs = md.digest(s.getBytes()); // 进行加密运算并返回字符数组

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bs.length; i++) { // 字节数组转换成十六进制字符串，形成最终的密文
			int v = bs[i] & 0xff;
			if (v < 16) {
				sb.append(0);
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString();
	}

	public static void responseJson(HttpServletResponse response, String content) {
		if (StringUtils.isBlank(content))
			return;
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		try {
			PrintWriter out = response.getWriter();
			out.append(content);
		} catch (IOException e) {
			logger.error("发送json数据异常，要发送的内容是：" + content);
		}

	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmptyList(List list) {
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}

	public static boolean isEmptyArray(Object[] arrays) {
		if (arrays != null && arrays.length > 0) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("rawtypes")
	public static boolean isEmptyMap(Map map) {
		if (map != null && map.size() > 0) {
			return false;
		}
		return true;
	}

	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str)) {
			return true;
		} else {
			return false;
		}

	}

	public static Date parse(String dateStr, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			Date date = sdf.parse(dateStr);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dateStr = sdf.format(date);
		return dateStr;
	}

	public static Date getFirstDayOfYear(String year) throws ParseException {
		Date date = null;
		if (StringUtils.isNumeric(year) && year.length() == 4) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			date = sdf.parse(year + "-01-01 00:00:00");
		}
		return date;
	}

	public static Date getLastDayOfYear(String year) throws ParseException {
		Date date = null;
		if (StringUtils.isNumeric(year) && year.length() == 4) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			date = sdf.parse(year + "-12-31 23:59:59");
		}
		return date;
	}

	/**
	 * 替换字符串中html标签
	 */
	public static String replaceHtml(String str) {
		if (str != null && !"".equals(str)) {
			str = str.replaceAll("<[^>]+>", "");
			return str;
		}
		return str;
	}

	public static String getLimitHtmlString(String str, int len) {
		if (str != null && !"".equals(str)) {
			str = getLimitString(str.replaceAll(" ", "").replaceAll("<[^>]+>", "").replaceAll("\r\n", "").replaceAll("&nbsp;", ""), len, true);
			return str;
		}
		return str;
	}

	public static String getLimitString(String str, int len, boolean suffix) {
		String result = "";
		try {
			int counterOfDoubleByte = 0;
			byte[] b = str.getBytes("gbk");
			if (b.length <= len)
				return str;
			for (int i = 0; i < len; i++) {
				if (b[i] < 0)
					counterOfDoubleByte++;
			}
			if (counterOfDoubleByte % 2 == 0)
				result = new String(b, 0, len, "gbk");
			else
				result = new String(b, 0, len - 1, "gbk");
		} catch (Exception ex) {
		}
		if (suffix && len > 0)
			result += "...";
		return result;
	}

	/**
	 * 标签转义
	 * 
	 * @param content
	 * @return
	 */
	public static String html(String content) {

		if (content == null)
			return "";
		String html = content;
		html = content.replace("&#39;", "&apos;");
		html = content.replace("\"", "&quot;");
		html = content.replace("\t", "&nbsp;&nbsp;");// 替换跳格
		// html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
		html = content.replace("<", "&lt;");
		html = content.replace(">", "&gt;");
		return html;
	}

	/**
	 * 对Properties文件加载的方法，可以一次性加载多个Properties文件。
	 * </p>
	 * 只需要知道Properties的名字(但是必须要保证在classpath下面)
	 * 
	 */
	public static class CustomPropertiesLoaderUtils extends PropertiesLoaderUtils {
	}

	/**
	 * JavaBean的操作帮助类。可以构造示例，查找方法，等。在我们平常的编程中最有用的方法则是copyProperties。
	 * </p>
	 * 他可以对两个对象的属性值进行复制，且能明确的告诉你source与target，这点比apache BeanUtil容易区分
	 * 
	 */
	public static class CustomBeanUtils extends BeanUtils {

		/**
		 * 拷贝一个List到另外一个List中
		 * 
		 * @param source
		 * @param target
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public static <T> List<T> copyListToList(List<?> source, Class<T> target, String[] ignoreProperties) {
			List<T> targets = new ArrayList<T>();
			try {
				for (Object object : source) {
					Object targetObj = target.newInstance();
					copyProperties(object, targetObj, ignoreProperties);
					targets.add((T) targetObj);
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return targets;
		}

		public static <T> List<T> copyListToList(List<?> source, Class<T> target) {
			return copyListToList(source, target, null);
		}

		/**
		 * copy值到JavaBean中，暂时只支持String to String
		 * 
		 * @Title: copyPropertiesToBean
		 * @param properties
		 * @param target
		 */
		public static void copyPropertiesToBean(Properties properties, Object target) {
			Assert.notNull(properties, "Properties must not be null");
			Assert.notNull(target, "Target must not be null");
			Class<?> targetClass = target.getClass();
			PropertyDescriptor[] targetPds = getPropertyDescriptors(targetClass);
			for (PropertyDescriptor propertyDescriptor : targetPds) {
				if (propertyDescriptor.getWriteMethod() != null) {
					try {
						Object obj = properties.get(propertyDescriptor.getName());
						Method writeMethod = propertyDescriptor.getWriteMethod();
						if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
							writeMethod.setAccessible(true);
						}
						Class<?> paramClass = writeMethod.getParameterTypes()[0];
						if ("java.lang.String".equals(paramClass.getName())) {
							writeMethod.invoke(target, obj);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}

		/**
		 * 将java对象转换为map对象
		 * 
		 * @param obj
		 * @return
		 */
		public static Map<String, Object> transBean2Map(Object obj) {
			if (obj == null) {
				return null;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor property : propertyDescriptors) {
					String key = property.getName();
					// 过滤class属性
					if (!key.equals("class")) {
						// 得到property对应的getter方法
						Method getter = property.getReadMethod();
						Object value = getter.invoke(obj);
						map.put(key, value);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return map;

		}

		/**
		 * 将java对象转换为map对象
		 * 
		 * @param obj
		 * @return
		 */
		public static Map<String, Object> transBean2MapContain(Object obj, String[] containProperties) {
			if (obj == null) {
				return null;
			}
			String ignoreProStr = CustomBeanUtils.transArrayToString(containProperties);
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor property : propertyDescriptors) {
					String key = property.getName();
					// 过滤class属性
					if (!key.equals("class") && ignoreProStr.indexOf(key) >= 0) {
						// 得到property对应的getter方法
						Method getter = property.getReadMethod();
						Object value = getter.invoke(obj);
						map.put(key, value);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return map;

		}

		public static Map<String, Object> transBean2MapIgnore(Object obj, String[] ignoreProperties) {
			if (obj == null) {
				return null;
			}
			String ignoreProStr = CustomBeanUtils.transArrayToString(ignoreProperties);
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor property : propertyDescriptors) {
					String key = property.getName();
					// 过滤class属性
					if (!key.equals("class") && ignoreProStr.indexOf(key) < 0) {
						// 得到property对应的getter方法
						Method getter = property.getReadMethod();
						Object value = getter.invoke(obj);
						map.put(key, value);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return map;

		}

		public static List<Map<String, Object>> transBean2MapListContain(List list, String[] containProperties) {
			if (CommonUtil.isEmptyList(list)) {
				return null;
			}
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Object object : list) {
				Map<String, Object> map = transBean2MapContain(object, containProperties);
				mapList.add(map);
			}
			return mapList;
		}

		public static List<Map<String, Object>> transBean2MapListIgnore(List list, String[] ignoreProperties) {
			if (CommonUtil.isEmptyList(list)) {
				return null;
			}
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for (Object object : list) {
				Map<String, Object> map = transBean2MapIgnore(object, ignoreProperties);
				mapList.add(map);
			}
			return mapList;
		}

		public static String transArrayToString(String[] arr) {
			if (arr != null && arr.length > 0) {
				StringBuffer sbf = new StringBuffer();
				for (int i = 0; i < arr.length; i++) {
					if (i == 0) {
						sbf.append(arr[i]);
					} else {
						sbf.append("," + arr[i]);
					}
				}
				return sbf.toString();
			}
			return "";
		}

		/**
		 * 将java对象转换为map对象
		 * 
		 * @param obj
		 * @return
		 */
		/*
		 * public static Map<String, Object> transBean2ListMap(List<Object> objList) { if (obj == null) { return null; } Map<String, Object> map = new
		 * HashMap<String, Object>(); try { BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass()); PropertyDescriptor[] propertyDescriptors =
		 * beanInfo.getPropertyDescriptors(); for (PropertyDescriptor property : propertyDescriptors) { String key = property.getName(); // 过滤class属性 if
		 * (!key.equals("class")) { // 得到property对应的getter方法 Method getter = property.getReadMethod(); Object value = getter.invoke(obj); map.put(key, value); }
		 * } } catch (Exception e) { throw new RuntimeException(e.getMessage(), e); } return map;
		 * 
		 * }
		 */

		public static void transMap2Bean(Map<String, Object> map, Object obj) {

			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

				for (PropertyDescriptor property : propertyDescriptors) {
					String key = property.getName();

					if (map.containsKey(key)) {
						Object value = map.get(key);
						// 得到property对应的setter方法
						Method setter = property.getWriteMethod();
						setter.invoke(obj, value);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return;
		}
	}

	public static boolean isInteger(String str) {
		try {
			int num = Integer.valueOf(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isNumber(String str) {
		try {
			new BigDecimal(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String convertLongToDateString(long time, String pattern) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(cal.getTime());
	}

	public static String getIpAddr(HttpServletRequest request) {

		String fromSource = "User-Invoke-Real-Ip";
		String ip = request.getHeader(fromSource);
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromSource = "X-Forwarded-For";
			ip = request.getHeader(fromSource);
			if (ip != null && ip.length() > 0) {
				StringBuffer mulitIp = new StringBuffer();
				String[] ips = ip.split(",");
				for (int i = 0; i < ips.length; i++) {
					mulitIp.append(ips[i].trim());
					if (i < ips.length - 1)
						mulitIp.append("-");
				}
				ip = mulitIp.toString();
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromSource = "X-Real-IP";
			ip = request.getHeader(fromSource);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromSource = "Proxy-Client-IP";
			ip = request.getHeader(fromSource);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromSource = "WL-Proxy-Client-IP";
			ip = request.getHeader(fromSource);
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			fromSource = "request.getRemoteAddr";
			ip = request.getRemoteAddr();
		}
		return ip;

	}

	public static <T> T clone(T source) throws IOException, ClassNotFoundException {
		// 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(source);
		// 将流序列化成对象
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (T) ois.readObject();
	}

}
