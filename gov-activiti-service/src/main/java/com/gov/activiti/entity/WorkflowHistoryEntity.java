package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_history")
@Schema(description = "流程历史扩展")
public class WorkflowHistoryEntity extends BaseEntity {

  @Schema(description = "流程实例ID")
  private String instanceId;

  @Schema(description = "流程定义Key")
  private String processKey;

  @Schema(description = "办件编号")
  private String applyNo;

  @Schema(description = "归档时间")
  private LocalDateTime archiveTime;

  @Schema(description = "归档人ID")
  private Long archiveBy;

  @Schema(description = "总耗时（毫秒）")
  private Long totalDuration;

  @Schema(description = "节点数量")
  private Integer nodeCount;

  @Schema(description = "审批次数")
  private Integer approveCount;

  @Schema(description = "驳回次数")
  private Integer rejectCount;

  @Schema(description = "流程结果")
  private String result;

  @Schema(description = "备注")
  private String remark;
}
