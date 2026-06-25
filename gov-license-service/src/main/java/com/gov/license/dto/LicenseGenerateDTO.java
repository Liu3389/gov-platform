package com.gov.license.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "证照生成请求")
public class LicenseGenerateDTO {

  @NotBlank(message = "办件编号不能为空")
  @Schema(description = "办件编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024SZ001001")
  private String applyNo;

  @NotNull(message = "事项ID不能为空")
  @Schema(description = "事项ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
  private Long itemId;

  @Schema(description = "事项名称", example = "身份证办理")
  private String itemName;

  @NotNull(message = "申请人ID不能为空")
  @Schema(description = "申请人ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10001")
  private Long userId;

  @NotBlank(message = "申请人姓名不能为空")
  @Schema(description = "申请人姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
  private String userName;

  @NotBlank(message = "证照目录编码不能为空")
  @Schema(description = "证照目录编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "ID_CARD")
  private String catalogCode;
}
