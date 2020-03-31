package com.oruit.share.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Component("baseApplicationContextUtil")
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext context;
	private static ThreadLocal<TransactionValue> threadLocal = new ThreadLocal<TransactionValue>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static <T> T getBean(Class<T> clazz) {
		if (context == null) {
			return null;
		}
		return context.getBean(clazz);
	}

	public static <T> T getBean(String beanName, Class<T> clazz) {
		if (context == null) {
			return null;
		}
		return context.getBean(beanName, clazz);
	}

	public static void beginTransaction() {
		// 1.获取事务控制管理器
		DataSourceTransactionManager transactionManager = context.getBean(DataSourceTransactionManager.class);
		// 2.获取事务定义
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		// 3.设置事务隔离级别，开启新事务
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		// 4.获得事务状态
		TransactionStatus status = transactionManager.getTransaction(def);
		TransactionValue transactionValue = new TransactionValue(transactionManager, status);
		threadLocal.set(transactionValue);
	}

	public static void commit() {
		TransactionValue value = threadLocal.get();
		value.getTransactionManager().commit(value.getStatus());
		value.getTransactionManager().rollback(value.getStatus());
	}

	public static void rollback() {
		TransactionValue value = threadLocal.get();
		value.getTransactionManager().rollback(value.getStatus());
	}
}
