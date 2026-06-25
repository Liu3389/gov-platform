package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_reminder")
@Schema(description = "催办记录")
public class WorkflowReminderEntity extends BaseEntity {

  @Schema(description = "流程实例ID")
  private String instanceId;

  @Schema(description = "任务ID")
  private String taskId;

  @Schema(description = "办件编号")
  private String applyNo;

  @Schema(description = "催办类型")
  private String reminderType;

  @Schema(description = "催办级别")
  private String reminderLevel;

  @Schema(description = "催办时间")
  private LocalDateTime reminderTime;

  @Schema(description = "催办人ID")
  private Long reminderBy;

  @Schema(description = "催办内容")
  private String reminderContent;

  @Schema(description = "状态：0未处理 1已处理")
  private String status;
}
