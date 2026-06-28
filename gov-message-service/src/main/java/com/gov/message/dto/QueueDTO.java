package com.gov.message.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.message.entity.QueueEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 消息队列入参 DTO
 */
@Data
@Schema(description = "消息队列请求")
public class QueueDTO {

    @Schema(description = "队列ID（修改时必填）", example = "1")
    private Long id;

    @NotNull(message = "消息记录ID不能为空")
    @Schema(description = "消息记录ID", required = true, example = "1")
    private Long recordId = 1L;

    @Schema(description = "队列状态：0待发送 1发送中 2成功 3失败", example = "0")
    private String queueStatus = "0";

    @Schema(description = "优先级：0普通 1紧急", example = "0")
    private Integer priority = 0;

    @Schema(description = "计划发送时间", example = "2026-06-25T10:00:00")
    private LocalDateTime scheduledTime = LocalDateTime.of(2026, 6, 25, 10, 0, 0);

    @Schema(description = "最大重试次数", example = "3")
    private Integer maxRetry = 3;

    public QueueEntity toEntity() {
        QueueEntity entity = new QueueEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
