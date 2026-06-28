package com.gov.open.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 通知公告DTO
 */
@Data
@Schema(description = "通知公告请求")
public class NoticeDTO {

    @NotBlank(message = "公告标题不能为空")
    @Schema(description = "公告标题", required = true)
    private String title;

    @Schema(description = "公告内容")
    private String content;

    @Schema(description = "公告类型")
    private String noticeType;

    @Schema(description = "是否置顶：0否 1是")
    private Integer topFlag;

    @Schema(description = "状态：0草稿 1已发布")
    private String status;

    /**
     * 转为 Entity
     */
    public com.gov.open.entity.NoticeEntity toEntity() {
        com.gov.open.entity.NoticeEntity entity = new com.gov.open.entity.NoticeEntity();
        entity.setTitle(this.title);
        entity.setContent(this.content);
        entity.setNoticeType(this.noticeType);
        entity.setTopFlag(this.topFlag);
        entity.setStatus(this.status);
        return entity;
    }
}
