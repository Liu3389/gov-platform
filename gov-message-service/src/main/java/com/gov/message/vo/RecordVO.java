package com.gov.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息记录出参 VO
 */
@Data
@Schema(description = "消息记录信息")
public class RecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "模板ID")
    private Long templateId;

    @Schema(description = "模板编码")
    private String templateCode;

    @Schema(description = "接收人ID")
    private Long receiverId;

    @Schema(description = "接收人姓名")
    private String receiverName;

    @Schema(description = "接收人电话[脱敏]")
    private String receiverPhone;

    @Schema(description = "接收人邮箱")
    private String receiverEmail;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "发送渠道：inbox/sms/email/app")
    private String channel;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "业务ID")
    private String businessId;

    @Schema(description = "发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "发送状态：0待发送 1发送中 2成功 3失败")
    private String sendStatus;

    @Schema(description = "发送消息")
    private String sendMsg;

    @Schema(description = "重试次数")
    private Integer retryCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
