package com.gov.activiti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "审批完成请求")
public class TaskCompleteDTO {

  @NotBlank(message = "任务ID不能为空")
  @Schema(description = "任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "task-001")
  private String taskId;

  @NotBlank(message = "审批结果不能为空")
  @Schema(description = "审批结果：1通过 2驳回 3转办", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
  private String approvalResult;

  @Schema(description = "审批意见", example = "材料齐全，同意办理")
  private String opinion;

  @Schema(description = "转办目标人ID（转办时必填）", example = "10002")
  private String targetAssignee;
}
