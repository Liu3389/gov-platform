package com.gov.license.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_license_data")
@Schema(description = "证照数据")
public class LicenseEntity extends BaseEntity {

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

    @Schema(description = "持证人身份证号 [脱敏][SM4加密]")
    private String userIdCard;

    @Schema(description = "关联办件号")
    private String applyNo;

    @Schema(description = "证照内容JSON")
    private String licenseContent;

    @Schema(description = "证照文件地址")
    private String fileUrl;

    @Schema(description = "二维码图片地址")
    private String qrCode;

    @Schema(description = "二维码内容")
    private String qrContent;

    @Schema(description = "签章时间")
    private LocalDateTime signTime;

    @Schema(description = "签章人ID")
    private Long signBy;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "证照状态：1有效 2过期 3注销")
    private String status;

    @Schema(description = "注销时间")
    private LocalDateTime cancelTime;

    @Schema(description = "注销原因")
    private String cancelReason;
}
