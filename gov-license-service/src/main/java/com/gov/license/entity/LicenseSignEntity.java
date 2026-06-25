package com.gov.license.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_license_sign")
@Schema(description = "签章记录")
public class LicenseSignEntity extends BaseEntity {

  @Schema(description = "证照ID")
  private Long licenseId;

  @Schema(description = "签章类型")
  private String signType;

  @Schema(description = "签章人ID")
  private Long signUser;

  @Schema(description = "签章人姓名")
  private String signUserName;

  @Schema(description = "签章时间")
  private LocalDateTime signTime;

  @Schema(description = "签章结果")
  private String signResult;

  @Schema(description = "证书序列号")
  private String signCertSn;

  @Schema(description = "签章数据")
  private String signData;
}
