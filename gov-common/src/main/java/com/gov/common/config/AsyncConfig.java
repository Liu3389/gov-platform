package com.gov.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步任务配置 —— 用于操作日志等非关键路径的异步处理
 *
 * <p>日志写入不阻塞主业务流程，使用 Spring Boot 默认线程池执行。</p>
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    // 使用 Spring Boot 默认的 TaskExecutor，避免与 Activiti 等框架的自定义线程池冲突
}
