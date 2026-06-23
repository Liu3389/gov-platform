package com.gov.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 工作流任务 VO（Feign 远程调用返回）
 */
@Data
@Schema(description = "工作流任务信息")
public class WorkflowTaskVO implements Serializable {

    @Schema(description = "ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
