package com.gov.license.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "证照核验请求")
public class LicenseVerifyDTO {

  @NotBlank(message = "证照编号不能为空")
  @Schema(description = "证照编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "LIC20240001")
  private String licenseNo;

  @Schema(description = "核验人ID", example = "10001")
  private Long verifyUserId;

  @Schema(description = "核验场景", example = "窗口核验")
  private String verifyScene;
}
