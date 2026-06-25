package com.gov.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 工作流任务 VO（Feign 远程调用返回）
 * 字段对齐 t_workflow_task 表结构
 */
@Data
@Schema(description = "工作流任务信息")
public class WorkflowTaskVO implements Serializable {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "流程实例ID")
    private String instanceId;

    @Schema(description = "流程定义Key")
    private String processKey;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "任务Key")
    private String taskKey;

    @Schema(description = "责任人")
    private String assignee;

    @Schema(description = "责任人姓名")
    private String assigneeName;

    @Schema(description = "认领时间")
    private LocalDateTime claimTime;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "到期时间")
    private LocalDateTime dueTime;

    @Schema(description = "耗时（毫秒）")
    private Long duration;

    @Schema(description = "任务状态")
    private String status;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
