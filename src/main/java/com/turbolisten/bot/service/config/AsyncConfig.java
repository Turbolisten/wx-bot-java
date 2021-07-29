package com.turbolisten.bot.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 异步调用线程配置
 *
 * @author Turbolisten
 * @date 2019/12/26 11:54
 */
@Slf4j
@Configuration
public class AsyncConfig {

    /**
     * 线程池 配置bean名称
     */
    public static final String ASYNC_EXECUTOR = "asyncExecutor";

    /**
     * 配置线程池
     *
     * @return
     */
    @Bean(name = ASYNC_EXECUTOR)
    public AsyncTaskExecutor executor() {
        int processors = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数量
        taskExecutor.setCorePoolSize(processors);
        // 最大线程数量
        taskExecutor.setMaxPoolSize(processors * 2);
        taskExecutor.setThreadNamePrefix(ASYNC_EXECUTOR);
        taskExecutor.initialize();
        return taskExecutor;
    }

    /**
     * spring 异步任务 异常配置
     */
    @Configuration
    public static class AsyncExceptionConfig implements AsyncConfigurer {
        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return new AsyncExceptionHandler();
        }
    }

    /**
     * 自定义异常处理
     */
    public static class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.error("异步任务发生异常:{}, 参数:{}, ", method.getDeclaringClass().getSimpleName() + "." + method.getName(), Arrays.toString(objects), throwable);
        }
    }
}
