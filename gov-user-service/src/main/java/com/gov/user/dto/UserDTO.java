package com.gov.user.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户新增/修改请求 DTO
 */
@Data
@Schema(description = "用户请求")
public class UserDTO {

    @Schema(description = "用户ID（修改时必填）")
    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", required = true)
    private String username;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "状态：0正常 1禁用 2待实名")
    private Integer status;

    public UserEntity toEntity() {
        return BeanUtil.copyProperties(this, UserEntity.class);
    }
}
