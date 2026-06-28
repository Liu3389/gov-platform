package com.gov.message.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.message.entity.InboxEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 站内信入参 DTO
 */
@Data
@Schema(description = "站内信请求")
public class InboxDTO {

    @Schema(description = "站内信ID（修改时必填）", example = "1")
    private Long id;

    @NotBlank(message = "消息标题不能为空")
    @Schema(description = "消息标题", required = true, example = "系统通知")
    private String title = "系统通知";

    @NotBlank(message = "消息内容不能为空")
    @Schema(description = "消息内容", required = true, example = "您有一条新的待办事项")
    private String content = "您有一条新的待办事项";

    @Schema(description = "消息类型", example = "系统通知")
    private String msgType = "系统通知";

    @Schema(description = "业务类型", example = "complaint")
    private String businessType = "complaint";

    @Schema(description = "业务ID", example = "1")
    private String businessId = "1";

    @Schema(description = "是否已读：0未读 1已读", example = "0")
    private Integer isRead = 0;

    @Schema(description = "发送时间", example = "2026-06-25T10:00:00")
    private LocalDateTime sendTime = LocalDateTime.of(2026, 6, 25, 10, 0, 0);

    @Schema(description = "过期时间", example = "2026-12-31T23:59:59")
    private LocalDateTime expireTime = LocalDateTime.of(2026, 12, 31, 23, 59, 59);

    public InboxEntity toEntity() {
        InboxEntity entity = new InboxEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
