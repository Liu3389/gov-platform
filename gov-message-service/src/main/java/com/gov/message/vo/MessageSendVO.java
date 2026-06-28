package com.gov.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息发送结果 VO
 */
@Data
@Schema(description = "消息发送结果")
public class MessageSendVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "发送记录ID")
    private Long recordId;

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "接收人ID")
    private Long receiverId;

    @Schema(description = "接收人姓名")
    private String receiverName;

    @Schema(description = "发送渠道")
    private List<String> channels;

    @Schema(description = "发送状态：0待发送 1发送中 2成功 3失败")
    private String sendStatus;

    @Schema(description = "发送消息")
    private String sendMsg;

    @Schema(description = "发送时间")
    private LocalDateTime sendTime;
}
