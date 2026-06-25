package com.gov.user.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 注册请求 DTO
 */
@Data
@Schema(description = "注册请求")
public class RegisterDTO {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名", example = "zhangsan")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", example = "123456")
    private String password;

    @NotBlank(message = "手机号不能为空")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    public UserEntity toEntity() {
        UserEntity entity = BeanUtil.copyProperties(this, UserEntity.class);
        entity.setLastLoginTime(java.time.LocalDateTime.now());
        entity.setStatus(0);
        return entity;
    }
}
