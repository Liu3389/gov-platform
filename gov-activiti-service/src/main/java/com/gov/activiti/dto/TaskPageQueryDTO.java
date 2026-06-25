package com.gov.activiti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "任务分页查询请求")
public class TaskPageQueryDTO {

  @NotNull(message = "页码不能为空")
  @Min(value = 1, message = "页码最小为1")
  @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
  private Long pageNum;

  @NotNull(message = "每页大小不能为空")
  @Min(value = 1, message = "每页大小最小为1")
  @Max(value = 100, message = "每页最大100条")
  @Schema(description = "每页大小", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
  private Long pageSize;

  @Schema(description = "任务名称（模糊搜索）", example = "审批")
  private String taskName;

  @Schema(description = "流程定义Key", example = "apply_approval_v1")
  private String processKey;

  @Schema(description = "任务状态：0待处理 1处理中 2已完成", example = "0")
  private String status;

  @Schema(description = "责任人", example = "10001")
  private String assignee;
}
