package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_opinion")
@Schema(description = "审批意见")
public class WorkflowOpinionEntity extends BaseEntity {

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
}
