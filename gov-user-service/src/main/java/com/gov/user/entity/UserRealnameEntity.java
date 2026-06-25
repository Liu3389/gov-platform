package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 实名认证记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_realname")
@Schema(description = "实名认证记录")
public class UserRealnameEntity extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "身份证号（SM4加密）")
    private String idCard;

    @Schema(description = "认证类型：1身份证 2护照 3其他")
    private Integer certType;

    @Schema(description = "证件正面照片URL")
    private String certFrontUrl;

    @Schema(description = "证件背面照片URL")
    private String certBackUrl;

    @Schema(description = "认证状态：0待审 1通过 2驳回")
    private Integer verifyStatus;

    @Schema(description = "认证时间")
    private java.time.LocalDateTime verifyTime;

    @Schema(description = "审核人ID")
    private Long verifyBy;

    @Schema(description = "审核备注")
    private String verifyRemark;
}
