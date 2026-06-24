package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_login_log")
@Schema(description = "登录日志")
public class LoginLogEntity extends BaseEntity {

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "登录用户名")
    private String username;

    @Schema(description = "登录IP")
    private String loginIp;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "登录时间")
    private java.time.LocalDateTime loginTime;

    @Schema(description = "登录类型：1PC 2移动端 3窗口终端")
    private Integer loginType;

    @Schema(description = "登录状态：1成功 0失败")
    private Integer loginStatus;

    @Schema(description = "登录消息")
    private String loginMsg;
}
