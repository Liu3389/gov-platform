package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_delegate")
@Schema(description = "委托记录")
public class WorkflowDelegateEntity extends BaseEntity {

  @Schema(description = "任务ID")
  private String taskId;

  @Schema(description = "流程实例ID")
  private String instanceId;

  @Schema(description = "委托人ID")
  private Long fromUserId;

  @Schema(description = "委托人姓名")
  private String fromUserName;

  @Schema(description = "被委托人ID")
  private Long toUserId;

  @Schema(description = "被委托人姓名")
  private String toUserName;

  @Schema(description = "委托类型")
  private String delegateType;

  @Schema(description = "委托时间")
  private LocalDateTime delegateTime;

  @Schema(description = "委托原因")
  private String delegateReason;

  @Schema(description = "状态：0有效 1已撤销")
  private String status;
}
