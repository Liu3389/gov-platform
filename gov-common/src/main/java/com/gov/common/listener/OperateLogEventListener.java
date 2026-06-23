package com.gov.common.listener;

import com.gov.common.event.OperateLogEvent;
import com.gov.common.feign.OperateLogFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 操作日志事件监听器 —— 异步消费 OperateLogEvent，通过 Feign 落库到 gov-monitor-service
 *
 * <p>架构说明：
 * <ul>
 *   <li>OperateLogAspect（AOP）→ 发布 OperateLogEvent（同步、快速）</li>
 *   <li>OperateLogEventListener（本类）→ 异步消费事件 → Feign 调用 gov-monitor-service</li>
 * </ul>
 *
 * <p>异步执行，不阻塞主业务流程。Feign 调用失败时由 FallbackFactory 降级处理。</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OperateLogEventListener {

    private final OperateLogFeignClient operateLogFeignClient;

    /**
     * 异步处理操作日志事件
     */
    @Async
    @EventListener
    public void handleOperateLogEvent(OperateLogEvent event) {
        try {
            operateLogFeignClient.record(event);
            log.debug("[操作日志] 记录成功: module={}, action={}, user={}",
                    event.getModule(), event.getAction(), event.getUserName());
        } catch (Exception e) {
            log.error("[操作日志] 落库异常: module={}, action={}", event.getModule(), event.getAction(), e);
        }
    }
}
