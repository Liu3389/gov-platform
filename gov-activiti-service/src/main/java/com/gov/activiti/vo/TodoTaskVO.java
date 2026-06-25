package com.gov.activiti.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "待办任务信息（对审批人展示）")
public class TodoTaskVO implements Serializable {

  @Schema(description = "任务ID")
  private String taskId;

  @Schema(description = "任务名称")
  private String taskName;

  @Schema(description = "流程实例ID")
  private String instanceId;

  @Schema(description = "流程名称")
  private String processName;

  @Schema(description = "办件编号")
  private String applyNo;

  @Schema(description = "事项名称")
  private String itemName;

  @Schema(description = "申请人姓名")
  private String userName;

  @Schema(description = "开始时间")
  private LocalDateTime startTime;

  @Schema(description = "到期时间")
  private LocalDateTime dueTime;

  @Schema(description = "优先级")
  private Integer priority;
}
