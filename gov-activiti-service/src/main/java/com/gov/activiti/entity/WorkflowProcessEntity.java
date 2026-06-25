package com.gov.activiti.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_workflow_process")
@Schema(description = "流程定义扩展")
public class WorkflowProcessEntity extends BaseEntity {

  @Schema(description = "流程定义Key")
  private String processKey;

  @Schema(description = "流程名称")
  private String processName;

  @Schema(description = "流程分类")
  private String processCategory;

  @Schema(description = "BPMN文件地址")
  private String bpmnUrl;

  @Schema(description = "版本号")
  private Integer version;

  @Schema(description = "流程描述")
  private String description;

  @Schema(description = "状态：1启用 0禁用")
  private String status;
}
