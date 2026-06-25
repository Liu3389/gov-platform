package com.gov.message.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.message.entity.TemplateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 消息模板入参 DTO
 */
@Data
@Schema(description = "消息模板请求")
public class TemplateDTO {

    @Schema(description = "模板ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotBlank(message = "模板编码不能为空")
    @Schema(description = "模板编码", required = true, example = "SMS_VERIFY_CODE")
    private String templateCode = "SMS_VERIFY_CODE";

    @NotBlank(message = "模板名称不能为空")
    @Schema(description = "模板名称", required = true, example = "短信验证码模板")
    private String templateName = "短信验证码模板";

    @NotBlank(message = "模板内容不能为空")
    @Schema(description = "模板内容", required = true, example = "您的验证码为：${code}，有效期${minutes}分钟。")
    private String templateContent = "您的验证码为：${code}，有效期${minutes}分钟。";

    @Schema(description = "模板类型", example = "验证码")
    private String templateType = "验证码";

    @NotBlank(message = "发送渠道不能为空")
    @Schema(description = "渠道：inbox/sms/email/app", required = true, example = "sms")
    private String channel = "sms";

    @Schema(description = "模板变量JSON", example = "{\"code\": \"123456\", \"minutes\": \"5\"}")
    private String variables = "{\"code\": \"123456\", \"minutes\": \"5\"}";

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status = "1";

    /** 转为 Entity，供 Service 层使用 */
    public TemplateEntity toEntity() {
        TemplateEntity entity = new TemplateEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
