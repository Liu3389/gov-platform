package com.gov.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Schema(description = "实名认证申请请求")
public class UserRealnameDTO {

    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private Long userId;

    @NotBlank(message = "真实姓名不能为空")
    @Schema(description = "真实姓名")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证号格式不正确")
    @Schema(description = "身份证号")
    private String idCard;

    @Schema(description = "身份证正面照片URL")
    private String idCardFrontUrl;

    @Schema(description = "身份证反面照片URL")
    private String idCardBackUrl;

    @Schema(description = "手持身份证照片URL")
    private String handIdCardUrl;
}
