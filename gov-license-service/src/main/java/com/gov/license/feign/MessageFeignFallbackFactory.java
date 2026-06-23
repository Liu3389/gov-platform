package com.gov.license.feign;

import com.gov.common.result.Result;
import com.gov.common.sentinel.SentinelBlockHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageFeignFallbackFactory implements FallbackFactory<MessageFeignClient> {

    @Override
    public MessageFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-message-service 失败", cause);
        return dto -> SentinelBlockHandler.<Void>blockHandler("message-service", "send");
    }
}
