package com.gov.activiti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "催办请求")
public class TaskRemindDTO {

  @NotBlank(message = "流程实例ID不能为空")
  @Schema(description = "流程实例ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "inst-001")
  private String instanceId;

  @Schema(description = "任务ID", example = "task-001")
  private String taskId;

  @Schema(description = "催办内容", example = "请尽快处理该办件")
  private String reminderContent;
}
