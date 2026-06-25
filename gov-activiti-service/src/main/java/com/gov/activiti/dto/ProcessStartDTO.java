package com.gov.activiti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Schema(description = "启动流程请求")
public class ProcessStartDTO {

  @NotBlank(message = "流程定义Key不能为空")
  @Schema(description = "流程定义Key", requiredMode = Schema.RequiredMode.REQUIRED, example = "apply_approval_v1")
  private String processKey;

  @NotBlank(message = "办件编号不能为空")
  @Schema(description = "办件编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024SZ001001")
  private String applyNo;

  @Schema(description = "事项ID", example = "10001")
  private Long itemId;

  @Schema(description = "事项名称", example = "身份证办理")
  private String itemName;

  @Schema(description = "申请人ID", example = "10001")
  private Long userId;

  @Schema(description = "申请人姓名", example = "张三")
  private String userName;

  @Schema(description = "部门ID", example = "20001")
  private Long deptId;

  @Schema(description = "流程变量（附加参数）")
  private Map<String, Object> variables;
}
