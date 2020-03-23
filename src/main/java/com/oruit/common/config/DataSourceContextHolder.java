package com.oruit.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 切换数据源
 * @author:wabgyt
 */
public class DataSourceContextHolder {
	static Logger log = LoggerFactory.getLogger(DataSourceContextHolder.class);

	private static final ThreadLocal<String> local = new ThreadLocal<String>();

	public static ThreadLocal<String> getLocal() {
		return local;
	}

	/**
	 * 读可能是多个库
	 */
	public static void read() {
		local.set(DataSourceType.read.getType());
	}

	/**
	 * 写只有一个库
	 */
	public static void write() {
		log.debug("writewritewrite");
		local.set(DataSourceType.write.getType());
	}

	public static String getJdbcType() {
		return local.get();
	}
}
