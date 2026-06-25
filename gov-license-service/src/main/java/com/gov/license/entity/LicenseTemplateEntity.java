package com.gov.license.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_license_template")
@Schema(description = "证照模板")
public class LicenseTemplateEntity extends BaseEntity {

  @Schema(description = "证照目录ID")
  private Long catalogId;

  @Schema(description = "模板名称")
  private String templateName;

  @Schema(description = "模板地址")
  private String templateUrl;

  @Schema(description = "模板类型")
  private String templateType;

  @Schema(description = "字段配置JSON")
  private String fieldConfig;

  @Schema(description = "签章配置JSON")
  private String signConfig;

  @Schema(description = "版本号")
  private Integer version;

  @Schema(description = "状态：1启用 0禁用")
  private String status;
}
