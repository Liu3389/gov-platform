package com.gov.license.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 消息发送请求（证照生成完成后通知用户）
 * 字段结构对齐 gov-message-service 的 MessageSendDTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "消息发送请求（证照生成完成后通知用户）")
public class MessageSendDTO {

    @Schema(description = "模板ID", required = true, example = "1")
    private Long templateId;

    @Schema(description = "接收人ID", required = true, example = "10001")
    private Long receiverId;

    @Schema(description = "接收人姓名", example = "张三")
    private String receiverName;

    @Schema(description = "模板变量参数", example = "{\"userName\":\"张三\",\"itemName\":\"身份证办理\"}")
    private Map<String, String> variables;

    @Schema(description = "发送渠道列表", example = "[\"SITE_MSG\"]")
    private List<String> channels;

    @Schema(description = "业务类型", example = "LICENSE")
    private String businessType;

    @Schema(description = "业务ID", example = "10001")
    private String businessId;
}
