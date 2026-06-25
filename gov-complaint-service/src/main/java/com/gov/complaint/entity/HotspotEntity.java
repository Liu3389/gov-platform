package com.gov.complaint.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 热点分析实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_complaint_hotspot")
@Schema(description = "热点分析")
public class HotspotEntity extends BaseEntity {

    @Schema(description = "热点关键词")
    private String keyword;

    @Schema(description = "关键词出现次数")
    private Integer keywordCount;

    @Schema(description = "统计日期")
    private LocalDateTime statDate;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "趋势")
    private String trend;
}
