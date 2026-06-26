package com.gov.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 公开反馈实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_open_feedback")
@Schema(description = "公开反馈")
public class FeedbackEntity extends BaseEntity {

    @Schema(description = "内容类型")
    private String contentType;

    @Schema(description = "内容ID")
    private Long contentId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户姓名")
    private String userName;

    @Schema(description = "反馈类型")
    private String feedbackType;

    @Schema(description = "反馈内容")
    private String feedbackContent;

    @Schema(description = "状态：0待处理 1已处理 2已关闭")
    private String status;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    private String handleResult;
}
