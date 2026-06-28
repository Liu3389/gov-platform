package com.gov.license.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "消息发送请求（证照生成完成后通知用户）")
public class MessageSendDTO {

  @Schema(description = "消息标题", example = "证照办理完成通知")
  private String title;

  @Schema(description = "消息内容", example = "您的证照已生成，证照编号：LIC20240101ABCDEF")
  private String content;

  @Schema(description = "接收人ID", example = "10001")
  private Long receiverId;

  @Schema(description = "发送渠道：INNER=站内信 SMS=短信 EMAIL=邮件", example = "INNER")
  private String channel;

  @Schema(description = "证照编号", example = "LIC20240101ABCDEF")
  private String licenseNo;

  @Schema(description = "事项名称", example = "身份证办理")
  private String itemName;

  @Schema(description = "办结时间", example = "2024-01-01 10:00:00")
  private String completeTime;
}
