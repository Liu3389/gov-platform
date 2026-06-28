package com.gov.message.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.message.entity.RecordEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 消息记录入参 DTO
 */
@Data
@Schema(description = "消息记录请求")
public class RecordDTO {

    @Schema(description = "记录ID（修改时必填）", example = "1")
    private Long id;

    @Schema(description = "模板ID", example = "1")
    private Long templateId = 1L;

    @Schema(description = "模板编码", example = "SMS_VERIFY_CODE")
    private String templateCode = "SMS_VERIFY_CODE";

    @Schema(description = "接收人ID", example = "100")
    private Long receiverId = 100L;

    @Schema(description = "接收人姓名", example = "张三")
    private String receiverName = "张三";

    @Schema(description = "接收人电话[脱敏]", example = "13800138000")
    private String receiverPhone = "13800138000";

    @Schema(description = "接收人邮箱", example = "zhangsan@example.com")
    private String receiverEmail = "zhangsan@example.com";

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "消息内容", required = true, example = "您的申请已通过审核")
    private String content = "您的申请已通过审核";

    @NotBlank(message = "发送渠道不能为空")
    @Schema(description = "发送渠道：inbox/sms/email/app", required = true, example = "sms")
    private String channel = "sms";

    @Schema(description = "业务类型", example = "complaint")
    private String businessType = "complaint";

    @Schema(description = "业务ID", example = "1")
    private String businessId = "1";

    @Schema(description = "发送时间", example = "2026-06-25T10:00:00")
    private LocalDateTime sendTime = LocalDateTime.of(2026, 6, 25, 10, 0, 0);

    @Schema(description = "发送状态：0待发送 1发送中 2成功 3失败", example = "0")
    private String sendStatus = "0";

    @Schema(description = "发送消息", example = "")
    private String sendMsg = "";

    @Schema(description = "重试次数", example = "0")
    private Integer retryCount = 0;

    public RecordEntity toEntity() {
        RecordEntity entity = new RecordEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
