package com.gov.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息配置出参 VO
 */
@Data
@Schema(description = "消息配置信息")
public class ConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "配置ID")
    private Long id;

    @Schema(description = "渠道：inbox/sms/email/app")
    private String channel;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "配置值[脱敏]")
    private String configValue;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "服务提供方")
    private String provider;

    @Schema(description = "状态：1启用 0禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
