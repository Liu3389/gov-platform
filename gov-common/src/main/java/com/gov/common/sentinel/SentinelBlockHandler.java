package com.gov.common.sentinel;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.gov.common.result.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * Sentinel 流控/熔断降级统一处理类
 * 所有 Feign 接口的 fallback 可继承此类复用
 */
@Slf4j
public class SentinelBlockHandler {

    /**
     * 限流/熔断通用降级
     */
    public static <T> Result<T> blockHandler(String serviceName, String method, BlockException ex) {
        log.error("[Sentinel] 服务 [{}] 接口 [{}] 被限流/熔断", serviceName, method, ex);
        return Result.fail(503, String.format("服务 [%s] 繁忙，请稍后重试。详情：%s", serviceName, ex.getMessage()));
    }

    /**
     * 限流/熔断通用降级（无异常信息）
     */
    public static <T> Result<T> blockHandler(String serviceName, String method) {
        log.error("[Sentinel] 服务 [{}] 接口 [{}] 被限流/熔断", serviceName, method);
        return Result.fail(503, String.format("服务 [%s] 繁忙，请稍后重试", serviceName));
    }
}
