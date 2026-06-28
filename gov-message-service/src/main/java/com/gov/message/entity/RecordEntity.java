package com.gov.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 消息记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_message_record")
@Schema(description = "消息记录")
public class RecordEntity extends BaseEntity {

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
}
