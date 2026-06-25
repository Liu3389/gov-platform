package com.gov.complaint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "建议征集信息")
public class SuggestionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "建议ID") private Long id;
    @Schema(description = "建议编号") private String suggestionNo;
    @Schema(description = "建议人ID") private Long userId;
    @Schema(description = "建议人姓名") private String userName;
    @Schema(description = "建议标题") private String title;
    @Schema(description = "建议内容") private String content;
    @Schema(description = "建议类型") private String suggestionType;
    @Schema(description = "状态：0待处理 1已回复 2已采纳") private String status;
    @Schema(description = "回复内容") private String replyContent;
    @Schema(description = "回复时间") private LocalDateTime replyTime;
    @Schema(description = "回复人ID") private Long replyBy;
    @Schema(description = "创建时间") private LocalDateTime createTime;
    @Schema(description = "更新时间") private LocalDateTime updateTime;
}
