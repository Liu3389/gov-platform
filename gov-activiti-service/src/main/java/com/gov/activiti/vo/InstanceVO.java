package com.gov.activiti.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "流程实例信息")
public class InstanceVO implements Serializable {

  @Schema(description = "ID")
  private Long id;

  @Schema(description = "流程实例ID（Activiti 实例ID）")
  private String instanceId;

  @Schema(description = "流程定义Key")
  private String processKey;

  @Schema(description = "办件编号")
  private String applyNo;

  @Schema(description = "事项ID")
  private Long itemId;

  @Schema(description = "事项名称")
  private String itemName;

  @Schema(description = "申请人ID")
  private Long userId;

  @Schema(description = "申请人姓名")
  private String userName;

  @Schema(description = "部门ID")
  private Long deptId;

  @Schema(description = "开始时间")
  private LocalDateTime startTime;

  @Schema(description = "结束时间")
  private LocalDateTime endTime;

  @Schema(description = "耗时（毫秒）")
  private Long duration;

  @Schema(description = "流程状态：0运行中 1已完成 2已终止 3已挂起")
  private String status;

  @Schema(description = "流程结果")
  private String result;

  @Schema(description = "创建时间")
  private LocalDateTime createTime;
}
