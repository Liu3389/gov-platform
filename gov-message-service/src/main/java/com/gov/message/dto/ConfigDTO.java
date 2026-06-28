package com.gov.message.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.message.entity.ConfigEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 消息配置入参 DTO
 */
@Data
@Schema(description = "消息配置请求")
public class ConfigDTO {

    @Schema(description = "配置ID（修改时必填）", example = "1")
    private Long id;

    @NotBlank(message = "渠道不能为空")
    @Schema(description = "渠道：inbox/sms/email/app", required = true, example = "sms")
    private String channel = "sms";

    @Schema(description = "渠道名称", example = "短信渠道")
    private String channelName = "短信渠道";

    @NotBlank(message = "配置键不能为空")
    @Schema(description = "配置键", required = true, example = "sms.accessKeyId")
    private String configKey = "sms.accessKeyId";

    @Schema(description = "配置值[脱敏]", example = "******")
    private String configValue = "******";

    @Schema(description = "配置类型", example = "阿里云")
    private String configType = "阿里云";

    @Schema(description = "服务提供方", example = "阿里云")
    private String provider = "阿里云";

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status = "1";

    public ConfigEntity toEntity() {
        ConfigEntity entity = new ConfigEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
