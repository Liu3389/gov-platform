package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_info")
@Schema(description = "用户信息")
public class UserEntity extends BaseEntity {

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "密码（BCrypt加密）")
    private String password;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "身份证号（SM4加密）")
    private String idCard;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "性别：0未知 1男 2女")
    private Integer gender;

    @Schema(description = "状态：0正常 1禁用 2待实名")
    private Integer status;

    @Schema(description = "最后登录时间")
    private java.time.LocalDateTime lastLoginTime;

    @Schema(description = "最后登录IP")
    private String lastLoginIp;
}