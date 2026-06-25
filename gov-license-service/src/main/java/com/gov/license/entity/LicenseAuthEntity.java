package com.gov.license.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_license_auth")
@Schema(description = "授权记录")
public class LicenseAuthEntity extends BaseEntity {

  @Schema(description = "证照ID")
  private Long licenseId;

  @Schema(description = "授权人ID")
  private Long authUserId;

  @Schema(description = "被授权对象ID")
  private Long authTargetId;

  @Schema(description = "被授权对象名称")
  private String authTargetName;

  @Schema(description = "授权类型")
  private String authType;

  @Schema(description = "授权范围")
  private String authScope;

  @Schema(description = "授权时间")
  private LocalDateTime authTime;

  @Schema(description = "过期时间")
  private LocalDateTime expireTime;

  @Schema(description = "取消时间")
  private LocalDateTime cancelTime;

  @Schema(description = "授权状态：1有效 0已取消")
  private String status;
}
