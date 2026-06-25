package com.gov.activiti.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "审批意见信息")
public class OpinionVO implements Serializable {

  @Schema(description = "ID")
  private Long id;

  @Schema(description = "任务ID")
  private String taskId;

  @Schema(description = "流程实例ID")
  private String instanceId;

  @Schema(description = "办件编号")
  private String applyNo;

  @Schema(description = "操作人ID")
  private Long operatorId;

  @Schema(description = "操作人姓名")
  private String operatorName;

  @Schema(description = "意见类型：1通过 2驳回 3转办 4退回")
  private String opinionType;

  @Schema(description = "意见内容")
  private String opinionContent;

  @Schema(description = "操作时间")
  private LocalDateTime operateTime;

  @Schema(description = "下一处理人")
  private String nextAssignee;

  @Schema(description = "创建时间")
  private LocalDateTime createTime;
}
