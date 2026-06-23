package com.gov.common.feign;

import com.gov.common.event.OperateLogEvent;
import com.gov.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 操作日志 Feign 降级工厂
 *
 * <p>当日志服务不可用时，降级为只打印日志（不影响主业务）。
 * 操作日志记录失败不应阻塞正常的业务流程。</p>
 */
@Slf4j
@Component
public class OperateLogFeignFallbackFactory implements FallbackFactory<OperateLogFeignClient> {

    @Override
    public OperateLogFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-monitor-service 操作日志记录失败，降级处理", cause);
        return new OperateLogFeignClient() {
            @Override
            public Result<Void> record(OperateLogEvent event) {
                // 降级：仅打印 WARN 日志，不影响主业务
                log.warn("[日志降级] 操作日志落库失败: module={}, action={}, user={}",
                        event.getModule(), event.getAction(), event.getUserName());
                return Result.fail("操作日志服务暂不可用");
            }
        };
    }
}
