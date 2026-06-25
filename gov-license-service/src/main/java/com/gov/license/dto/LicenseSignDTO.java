package com.gov.license.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "证照签章请求（licenseId 由 URL 路径传入）")
public class LicenseSignDTO {

  @Schema(description = "证照ID（由 Controller 自动设置）")
  private Long licenseId;
  @NotBlank(message = "签章类型不能为空")
  @Schema(description = "签章类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "CA_SIGN")
  private String signType;
}
