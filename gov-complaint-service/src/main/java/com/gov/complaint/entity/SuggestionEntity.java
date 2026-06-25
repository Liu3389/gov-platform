package com.gov.complaint.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 建议征集实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_suggestion")
@Schema(description = "建议征集")
public class SuggestionEntity extends BaseEntity {

    @Schema(description = "建议编号")
    private String suggestionNo;

    @Schema(description = "建议人ID")
    private Long userId;

    @Schema(description = "建议人姓名")
    private String userName;

    @Schema(description = "建议标题")
    private String title;

    @Schema(description = "建议内容")
    private String content;

    @Schema(description = "建议类型")
    private String suggestionType;

    @Schema(description = "状态：0待处理 1已回复 2已采纳")
    private String status;

    @Schema(description = "回复内容")
    private String replyContent;

    @Schema(description = "回复时间")
    private LocalDateTime replyTime;

    @Schema(description = "回复人ID")
    private Long replyBy;
}
