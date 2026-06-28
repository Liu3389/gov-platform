package com.gov.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消息队列实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_message_queue")
@Schema(description = "消息队列")
public class QueueEntity extends BaseEntity {

    @Schema(description = "消息记录ID")
    private Long recordId;

    @Schema(description = "队列状态：0待发送 1发送中 2成功 3失败")
    private String queueStatus;

    @Schema(description = "优先级：0普通 1紧急")
    private Integer priority;

    @Schema(description = "计划发送时间")
    private LocalDateTime scheduledTime;

    @Schema(description = "处理时间")
    private LocalDateTime processTime;

    @Schema(description = "已重试次数")
    private Integer retryCount;

    @Schema(description = "最大重试次数")
    private Integer maxRetry;

    @Schema(description = "错误信息")
    private String errorMsg;
}
