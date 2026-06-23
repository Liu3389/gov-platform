package com.gov.common.feign;

import com.gov.common.event.OperateLogEvent;
import com.gov.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 操作日志 Feign 客户端 —— gov-common 内部使用，向 gov-monitor-service 发送操作日志
 *
 * <p>此 Feign 客户端由 gov-common 提供，所有业务服务自动继承。
 * OperateLogEventListener 监听本地事件后通过此客户端异步落库。</p>
 */
@FeignClient(
        name = "gov-monitor-service",
        path = "/monitor",
        fallbackFactory = OperateLogFeignFallbackFactory.class
)
public interface OperateLogFeignClient {

    /**
     * 记录操作日志（内部接口，不对外暴露）
     */
    @PostMapping("/log/record")
    Result<Void> record(@RequestBody OperateLogEvent event);
}
