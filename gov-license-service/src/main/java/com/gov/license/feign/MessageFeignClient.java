package com.gov.license.feign;

import com.gov.common.result.Result;
import com.gov.license.dto.MessageSendDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 消息服务 Feign 客户端
 * gov-license-service → gov-message-service (证照生成后发送通知)
 */
@FeignClient(name = "gov-message-service", path = "/message", fallbackFactory = MessageFeignFallbackFactory.class)
public interface MessageFeignClient {

    @PostMapping("/send")
    Result<Void> send(@RequestBody MessageSendDTO messageDTO);
}
