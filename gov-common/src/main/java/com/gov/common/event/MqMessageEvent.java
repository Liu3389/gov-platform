package com.gov.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息队列事件基类 —— 统一消息格式，各服务通过 RabbitMQ 收发消息
 *
 * <p>消息格式：{ "type": "NOTIFY/WARNING/LOG", "source": "服务名", "data": {...}, "timestamp": ... }</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqMessageEvent {

    /** 消息类型：NOTIFY / WARNING / LOG */
    private String type;

    /** 来源服务名 */
    private String source;

    /** 消息体数据（JSON 字符串） */
    private String data;

    /** 消息时间戳（毫秒） */
    private Long timestamp;
}
