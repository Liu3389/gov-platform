package com.gov.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息配置实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_message_config")
@Schema(description = "消息配置")
public class ConfigEntity extends BaseEntity {

    @Schema(description = "渠道：inbox/sms/email/app")
    private String channel;

    @Schema(description = "渠道名称")
    private String channelName;

    @Schema(description = "配置键")
    private String configKey;

    @Schema(description = "配置值[脱敏][加密存储]")
    private String configValue;

    @Schema(description = "配置类型")
    private String configType;

    @Schema(description = "服务提供方")
    private String provider;

    @Schema(description = "状态：1启用 0禁用")
    private String status;
}
