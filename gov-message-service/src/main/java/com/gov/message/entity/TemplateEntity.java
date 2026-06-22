package com.gov.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息模板实体（示例）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_message_template")
@Schema(description = "消息模板")
public class TemplateEntity extends BaseEntity {

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "发送渠道：inbox/sms/email/app")
    private String channel;

    @Schema(description = "状态：1启用 0禁用")
    private Integer status;
}
