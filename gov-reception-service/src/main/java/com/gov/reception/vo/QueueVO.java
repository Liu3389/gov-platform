package com.gov.reception.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 排队VO
 */
@Data
@Schema(description = "排队信息")
public class QueueVO implements Serializable {

    @Schema(description = "排队ID")
    private Long id;

    @Schema(description = "排队号码")
    private String queueNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "窗口ID")
    private Long windowId;

    @Schema(description = "窗口名称")
    private String windowName;

    @Schema(description = "事项ID")
    private Long itemId;

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "排队时间")
    private LocalDateTime queueTime;

    @Schema(description = "叫号时间")
    private LocalDateTime callTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "状态：0等待中 1办理中 2已完成 3已取消")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "前面等待人数")
    private Integer waitCount;
}
