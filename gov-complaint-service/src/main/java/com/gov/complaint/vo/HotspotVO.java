package com.gov.complaint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "热点分析信息")
public class HotspotVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "热点ID") private Long id;
    @Schema(description = "热点关键词") private String keyword;
    @Schema(description = "关键词出现次数") private Integer keywordCount;
    @Schema(description = "统计日期") private LocalDateTime statDate;
    @Schema(description = "分类ID") private Long categoryId;
    @Schema(description = "趋势") private String trend;
    @Schema(description = "创建时间") private LocalDateTime createTime;
}
