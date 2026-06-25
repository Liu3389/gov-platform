package com.gov.complaint.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.complaint.entity.HotspotEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 热点分析入参 DTO
 */
@Data
@Schema(description = "热点分析请求")
public class HotspotDTO {

    @Schema(description = "热点ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotBlank(message = "关键词不能为空")
    @Schema(description = "热点关键词", required = true, example = "路灯维修")
    private String keyword = "路灯维修";

    @NotNull(message = "出现次数不能为空")
    @Schema(description = "关键词出现次数", required = true, example = "156")
    private Integer keywordCount = 156;

    @NotNull(message = "统计日期不能为空")
    @Schema(description = "统计日期", required = true, example = "2026-06-25T00:00:00")
    private LocalDateTime statDate = LocalDateTime.of(2026, 6, 25, 0, 0, 0);

    @Schema(description = "分类ID", example = "1")
    private Long categoryId = 1L;

    @Schema(description = "趋势", example = "上升")
    private String trend = "上升";

    public HotspotEntity toEntity() {
        HotspotEntity entity = new HotspotEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
