package com.productpulse.productpulse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

    @EnableAsync
    @Configuration
    public class AsyncConfig implements AsyncConfigurer {
        @Override
        public Executor getAsyncExecutor() {
            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
            executor.setCorePoolSize(5);
            executor.setMaxPoolSize(10);
            executor.setQueueCapacity(25);
            executor.setThreadNamePrefix("AsyncThread-");
            executor.initialize();
            return executor;
        }
    }
