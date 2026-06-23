package com.gov.common.config;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Sentinel Feign 整合配置
 * 启用 Feign 对 Sentinel 的支持，实现熔断降级
 */
@Configuration
public class SentinelFeignConfig {

    /**
     * Sentinel WebMVC 流控异常处理器
     * 被限流时返回统一 Result 格式（JSON）
     */
    @Bean
    public BlockExceptionHandler sentinelBlockExceptionHandler() {
        return (HttpServletRequest request, HttpServletResponse response, BlockException e) -> {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("code", 503);
            result.put("message", "服务繁忙，请稍后重试");
            result.put("data", null);
            result.put("timestamp", System.currentTimeMillis());
            result.put("traceId", "");
            PrintWriter out = response.getWriter();
            out.print(new ObjectMapper().writeValueAsString(result));
            out.flush();
        };
    }
}
