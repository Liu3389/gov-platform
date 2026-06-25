package com.gov.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息模板出参 VO
 */
@Data
@Schema(description = "消息模板信息")
public class TemplateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "模板ID")
    private Long id;

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "模板内容")
    private String templateContent;

    @Schema(description = "模板类型")
    private String templateType;

    @Schema(description = "渠道：inbox/sms/email/app")
    private String channel;

    @Schema(description = "模板变量JSON")
    private String variables;

    @Schema(description = "状态：1启用 0禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
