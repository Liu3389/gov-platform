package com.gov.reception.feign;

import com.gov.common.result.Result;
import com.gov.common.sentinel.SentinelBlockHandler;
import com.gov.common.vo.WorkflowTaskVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 审批流服务 Feign 降级工厂
 */
@Slf4j
@Component
public class ActivitiFeignFallbackFactory implements FallbackFactory<ActivitiFeignClient> {

    @Override
    public ActivitiFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-activiti-service 失败", cause);
        return dto -> SentinelBlockHandler.<WorkflowTaskVO>blockHandler("activiti-service", "startProcess");
    }
}
