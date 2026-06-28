package com.gov.message.consumer;

import com.alibaba.fastjson2.JSON;
import com.gov.message.dto.MessageSendDTO;
import com.gov.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消息发送 RabbitMQ 消费者
 * 监听 queue:message.send 队列，异步处理消息发送
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageSendConsumer {

    private final MessageService messageService;

    /**
     * 消费消息发送事件
     * 消息格式与 MessageSendDTO 一致
     */
    @RabbitListener(queues = "message.send")
    public void onMessage(String message) {
        log.info("[消息消费者] 收到消息发送请求：{}", message);
        int retryCount = 0;
        int maxRetry = 3;

        while (retryCount < maxRetry) {
            try {
                MessageSendDTO dto = JSON.parseObject(message, MessageSendDTO.class);
                if (dto == null || dto.getTemplateId() == null || dto.getReceiverId() == null) {
                    log.error("[消息消费者] 消息格式错误，无法解析：{}", message);
                    return;
                }
                messageService.sendMessage(dto);
                log.info("[消息消费者] 消息发送成功");
                return;
            } catch (Exception e) {
                retryCount++;
                log.error("[消息消费者] 消息发送失败，第{}次重试，原因：{}", retryCount, e.getMessage());
                if (retryCount >= maxRetry) {
                    log.error("[消息消费者] 消息发送失败，已达最大重试次数{}次，消息：{}", maxRetry, message);
                }
            }
        }
    }
}
