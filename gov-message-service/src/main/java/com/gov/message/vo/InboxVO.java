package com.gov.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 站内信出参 VO
 */
@Data
@Schema(description = "站内信信息")
public class InboxVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "站内信ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "消息标题")
    private String title;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "消息类型")
    private String msgType;

    @Schema(description = "业务类型")
    private String businessType;

    @Schema(description = "业务ID")
    private String businessId;

    @Schema(description = "是否已读：0未读 1已读")
    private Integer isRead;

    @Schema(description = "阅读时间")
    private LocalDateTime readTime;

    @Schema(description = "发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
