package com.gov.message.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 消息发送入参 DTO
 */
@Data
@Schema(description = "消息发送请求")
public class MessageSendDTO {

    @NotNull(message = "模板ID不能为空")
    @Schema(description = "模板ID", required = true, example = "1")
    private Long templateId;

    @NotNull(message = "接收人ID不能为空")
    @Schema(description = "接收人ID", required = true, example = "100")
    private Long receiverId;

    @Schema(description = "接收人姓名（可选，为空时自动获取当前用户）", example = "张三")
    private String receiverName;

    @Schema(description = "模板变量参数", example = "{\"userName\":\"张三\",\"itemName\":\"身份证办理\"}")
    private Map<String, String> variables;

    @NotEmpty(message = "发送渠道不能为空")
    @Schema(description = "发送渠道列表：SITE_MSG-站内信、SMS-短信、EMAIL-邮件、APP_PUSH-APP推送", required = true, example = "[\"SITE_MSG\"]")
    private List<String> channels;

    @Schema(description = "业务类型", example = "LICENSE")
    private String businessType;

    @Schema(description = "业务ID", example = "10001")
    private String businessId;
}
