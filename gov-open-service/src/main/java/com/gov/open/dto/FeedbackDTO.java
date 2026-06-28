package com.gov.open.dto;

import com.gov.open.entity.FeedbackEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 公开反馈DTO
 */
@Data
@Schema(description = "公开反馈请求")
public class FeedbackDTO {

    @NotNull(message = "内容类型不能为空")
    @Schema(description = "内容类型：1公告 2政策 3目录", required = true)
    private String contentType;

    @Schema(description = "内容ID")
    private Long contentId;

    @Schema(description = "反馈类型：1咨询 2建议 3投诉")
    private String feedbackType;

    @NotBlank(message = "反馈内容不能为空")
    @Schema(description = "反馈内容", required = true)
    private String feedbackContent;

    /**
     * 转为 Entity
     */
    public FeedbackEntity toEntity() {
        FeedbackEntity entity = new FeedbackEntity();
        entity.setContentType(this.contentType);
        entity.setContentId(this.contentId);
        entity.setFeedbackType(this.feedbackType);
        entity.setFeedbackContent(this.feedbackContent);
        return entity;
    }
}
