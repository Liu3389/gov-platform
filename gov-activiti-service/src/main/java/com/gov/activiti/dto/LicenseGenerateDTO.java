package com.gov.activiti.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "证照生成请求（Feign 调用参数）")
public class LicenseGenerateDTO {

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

  @Schema(description = "证照目录编码", example = "LICENSE_ID_CARD")
  private String catalogCode;
}
