package com.gov.license.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "证照分页查询请求")
public class LicensePageQueryDTO {

  @NotNull
  @Min(1)
  @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
  private Long pageNum;

  @NotNull
  @Min(1)
  @Max(value = 100, message = "每页最大100条")
  @Schema(description = "每页大小", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
  private Long pageSize;

  @Schema(description = "证照编号", example = "LIC20240001")
  private String licenseNo;

  @Schema(description = "持证人姓名", example = "张三")
  private String keyword;

  @Schema(description = "证照状态：1有效 2过期 3注销", example = "1")
  private String status;
}
