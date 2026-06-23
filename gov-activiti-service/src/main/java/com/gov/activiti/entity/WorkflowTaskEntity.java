package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_task")
@Schema(description = "工作流任务")
public class WorkflowTaskEntity extends BaseEntity {

    @Schema(description = "任务ID")
    private String taskId;

    @Schema(description = "申请编号")
    private String applyNo;

    @Schema(description = "任务名称")
    private String taskName;

    @Schema(description = "审批人ID")
    private Long assignee;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "状态")
    private Integer status;
}
