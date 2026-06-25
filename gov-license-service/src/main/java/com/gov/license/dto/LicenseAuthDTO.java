package com.gov.license.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "证照授权请求（licenseId 由 URL 路径传入）")
public class LicenseAuthDTO {

  @Schema(description = "证照ID（由 Controller 自动设置）")
  private Long licenseId;
  @NotNull(message = "被授权对象ID不能为空")
  @Schema(description = "被授权对象ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10002")
  private Long authTargetId;

  @Schema(description = "被授权对象名称", example = "李四")
  private String authTargetName;

  @NotBlank(message = "授权类型不能为空")
  @Schema(description = "授权类型：VIEW查看 DOWNLOAD下载", requiredMode = Schema.RequiredMode.REQUIRED, example = "VIEW")
  private String authType;
}
