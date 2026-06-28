package com.gov.common.utils;

import com.gov.common.event.MqMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ 消息发送工具类
 *
 * <p>提供点对点（队列）和发布订阅（交换机）两种发送方式。</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqSender {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 发送消息到指定队列
     *
     * @param queueName 队列名称
     * @param message   消息事件
     */
    public void send(String queueName, MqMessageEvent message) {
        log.info("发送消息到队列 [{}]，类型：{}，来源：{}", queueName, message.getType(), message.getSource());
        rabbitTemplate.convertAndSend(queueName, message);
    }

    /**
     * 发送消息到指定交换机（支持路由键）
     *
     * @param exchange   交换机名称
     * @param routingKey 路由键
     * @param message    消息事件
     */
    public void send(String exchange, String routingKey, MqMessageEvent message) {
        log.info("发送消息到交换机 [{}]，路由键：{}，类型：{}，来源：{}",
                exchange, routingKey, message.getType(), message.getSource());
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
