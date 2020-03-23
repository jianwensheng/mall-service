package com.oruit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.Executor;

@EnableTransactionManagement
@MapperScan("com.oruit.*.dao")
@SpringBootApplication
@EnableAsync
public class AppShareApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppShareApplication.class, args);
        System.out.println("商城启动成功..........");
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //如果池中的实际线程数小于corePoolSize,无论是否其中有空闲的线程，都会给新的任务产生新的线程
        taskExecutor.setCorePoolSize(2);
        //连接池中保留的最大连接数。
        taskExecutor.setMaxPoolSize(50);
        //queueCapacity 线程池所使用的缓冲队列
        taskExecutor.setQueueCapacity(10000);
        //强烈建议一定要给线程起一个有意义的名称前缀，便于分析日志
        taskExecutor.setThreadNamePrefix("calculate Thread-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
