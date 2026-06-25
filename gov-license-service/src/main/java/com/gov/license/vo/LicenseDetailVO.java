package com.gov.license.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "证照详情（含签章+核验记录）")
public class LicenseDetailVO implements Serializable {

  @Schema(description = "ID")
  private Long id;
  @Schema(description = "证照编号")
  private String licenseNo;
  @Schema(description = "证照目录ID")
  private Long catalogId;
  @Schema(description = "证照目录名称")
  private String catalogName;
  @Schema(description = "持证人ID")
  private Long userId;
  @Schema(description = "持证人姓名")
  private String userName;
  @Schema(description = "关联办件号")
  private String applyNo;
  @Schema(description = "证照文件地址")
  private String fileUrl;
  @Schema(description = "二维码图片地址")
  private String qrCode;
  @Schema(description = "签章时间")
  private LocalDateTime signTime;
  @Schema(description = "签章人ID")
  private Long signBy;
  @Schema(description = "过期时间")
  private LocalDateTime expireTime;
  @Schema(description = "证照状态：1有效 2过期 3注销")
  private String status;
  @Schema(description = "创建时间")
  private LocalDateTime createTime;
}
