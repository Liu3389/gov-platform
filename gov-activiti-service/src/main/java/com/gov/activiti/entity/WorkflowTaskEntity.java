package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_task")
@Schema(description = "工作流任务")
public class WorkflowTaskEntity extends BaseEntity {

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
}
