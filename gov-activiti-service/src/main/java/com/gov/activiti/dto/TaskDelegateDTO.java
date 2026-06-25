package com.gov.activiti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "任务委托请求")
public class TaskDelegateDTO {
  @NotNull(message = "被委托人ID不能为空")
  @Schema(description = "被委托人ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10002")
  private Long toUserId;

  @Schema(description = "委托原因", example = "出差代为审批")
  private String delegateReason;
}
