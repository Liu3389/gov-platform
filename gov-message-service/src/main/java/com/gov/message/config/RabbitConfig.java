package com.gov.message.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置
 * 声明所有需要的队列，确保服务启动时队列存在
 */
@Configuration
public class RabbitConfig {

    /**
     * 消息发送队列
     * durable=true 表示持久化，RabbitMQ 重启后队列不丢
     */
    @Bean
    public Queue messageSendQueue() {
        return new Queue("message.send", true);
    }
}
