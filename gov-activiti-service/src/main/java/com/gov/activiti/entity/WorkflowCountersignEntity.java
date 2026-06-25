package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_countersign")
@Schema(description = "会签记录")
public class WorkflowCountersignEntity extends BaseEntity {

  @Schema(description = "任务ID")
  private String taskId;

  @Schema(description = "流程实例ID")
  private String instanceId;

  @Schema(description = "会签人ID")
  private Long userId;

  @Schema(description = "会签人姓名")
  private String userName;

  @Schema(description = "部门ID")
  private Long deptId;

  @Schema(description = "部门名称")
  private String deptName;

  @Schema(description = "会签结果：1通过 2驳回")
  private String signResult;

  @Schema(description = "会签时间")
  private LocalDateTime signTime;

  @Schema(description = "会签意见")
  private String signOpinion;
}
