package com.gov.license.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_license_verify")
@Schema(description = "核验记录")
public class LicenseVerifyEntity extends BaseEntity {

  @Schema(description = "证照ID")
  private Long licenseId;

  @Schema(description = "证照编号")
  private String licenseNo;

  @Schema(description = "核验人ID")
  private Long verifyUser;

  @Schema(description = "核验人姓名")
  private String verifyUserName;

  @Schema(description = "核验时间")
  private LocalDateTime verifyTime;

  @Schema(description = "核验结果")
  private String verifyResult;

  @Schema(description = "核验场景")
  private String verifyScene;

  @Schema(description = "核验IP")
  private String verifyIp;

  @Schema(description = "核验备注")
  private String verifyRemark;
}
