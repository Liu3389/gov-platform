package com.gov.open.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 反馈VO
 */
@Data
@Schema(description = "反馈信息")
public class FeedbackVO implements Serializable {

    @Schema(description = "反馈ID")
    private Long id;

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

    @Schema(description = "反馈类型描述")
    private String feedbackTypeDesc;

    @Schema(description = "反馈内容")
    private String feedbackContent;

    @Schema(description = "状态：0待处理 1已处理 2已关闭")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "处理结果")
    private String handleResult;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
