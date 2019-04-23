package com.reven.task;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author reven
 */
@Configuration
@EnableAsync
public class AsyncConfig {
	private int corePoolSize = 2;
	private int maxPoolSize = 2;
	private int queueCapacity = 10;

	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setThreadNamePrefix("taskAsyncExecutor");
		executor.initialize();
		return executor;
	}
}
