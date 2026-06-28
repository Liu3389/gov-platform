package com.gov.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息队列出参 VO
 */
@Data
@Schema(description = "消息队列信息")
public class QueueVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "队列ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
