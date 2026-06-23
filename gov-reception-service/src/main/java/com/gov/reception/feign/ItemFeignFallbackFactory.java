package com.gov.reception.feign;

import com.gov.common.result.Result;
import com.gov.common.sentinel.SentinelBlockHandler;
import com.gov.common.vo.ItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 事项服务 Feign 降级工厂
 */
@Slf4j
@Component
public class ItemFeignFallbackFactory implements FallbackFactory<ItemFeignClient> {

    @Override
    public ItemFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-item-service 失败", cause);
        return id -> SentinelBlockHandler.<ItemVO>blockHandler("item-service", "getById");
    }
}
