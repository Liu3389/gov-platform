package com.gov.license.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_license_catalog")
@Schema(description = "证照目录")
public class LicenseCatalogEntity extends BaseEntity {

  @Schema(description = "证照目录编码")
  private String catalogCode;

  @Schema(description = "证照目录名称")
  private String catalogName;

  @Schema(description = "证照类型")
  private String catalogType;

  @Schema(description = "颁发部门ID")
  private Long deptId;

  @Schema(description = "颁发部门名称")
  private String deptName;

  @Schema(description = "模板地址")
  private String templateUrl;

  @Schema(description = "模板类型")
  private String templateType;

  @Schema(description = "有效期（年）")
  private Integer validYears;

  @Schema(description = "签章标记：0不需要 1需要")
  private Integer signFlag;

  @Schema(description = "二维码标记：0不需要 1需要")
  private Integer qrFlag;

  @Schema(description = "状态：1启用 0禁用")
  private String status;
}
