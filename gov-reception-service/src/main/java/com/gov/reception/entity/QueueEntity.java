package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 排队叫号实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_reception_queue")
@Schema(description = "排队叫号")
public class QueueEntity extends BaseEntity {

    @Schema(description = "排队号码")
    private String queueNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "窗口ID")
    private Long windowId;

    @Schema(description = "事项ID")
    private Long itemId;

    @Schema(description = "排队时间")
    private LocalDateTime queueTime;

    @Schema(description = "叫号时间")
    private LocalDateTime callTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "状态：0等待中 1办理中 2已完成 3已取消")
    private String status;

    @Schema(description = "优先级（数字越大优先级越高）")
    private Integer priority;
}
