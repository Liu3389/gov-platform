package com.gov.open.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 公告查询DTO
 */
@Data
@Schema(description = "公告查询请求")
public class NoticeQueryDTO {

    @Schema(description = "标题关键词")
    private String keyword;

    @Schema(description = "状态：0草稿 1已发布 2已撤回")
    private Integer status;
}
