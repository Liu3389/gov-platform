package com.gov.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "登录日志响应")
public class LoginLogVO implements Serializable {

    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "登录用户名")
    private String username;

    @Schema(description = "登录IP")
    private String loginIp;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;

    @Schema(description = "登录类型：1PC 2移动端 3窗口终端")
    private Integer loginType;

    @Schema(description = "登录状态：1成功 0失败")
    private Integer loginStatus;

    @Schema(description = "登录消息")
    private String loginMsg;
}
