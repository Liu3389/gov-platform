package com.gov.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "实名认证响应")
public class UserRealnameVO implements Serializable {

    @Schema(description = "认证ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "身份证号（脱敏）")
    private String idCard;

    @Schema(description = "身份证正面照片URL")
    private String idCardFrontUrl;

    @Schema(description = "身份证反面照片URL")
    private String idCardBackUrl;

    @Schema(description = "手持身份证照片URL")
    private String handIdCardUrl;

    @Schema(description = "审核状态：0待审 1通过 2驳回")
    private Integer verifyStatus;

    @Schema(description = "审核备注")
    private String verifyRemark;

    @Schema(description = "审核时间")
    private LocalDateTime verifyTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
