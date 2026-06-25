package com.gov.complaint.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.complaint.entity.CategoryEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 投诉分类入参 DTO
 */
@Data
@Schema(description = "投诉分类请求")
public class CategoryDTO {

    @Schema(description = "分类ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称", required = true, example = "市政设施")
    private String categoryName = "市政设施";

    @NotBlank(message = "分类编码不能为空")
    @Schema(description = "分类编码", required = true, example = "SZSS")
    private String categoryCode = "SZSS";

    @Schema(description = "父级ID", example = "0")
    private Long parentId = 0L;

    @Schema(description = "关键词", example = "路灯,道路,井盖")
    private String keywords = "路灯,道路,井盖";

    @Schema(description = "默认处理部门ID", example = "1")
    private Long defaultDeptId = 1L;

    @Schema(description = "排序", example = "1")
    private Integer sort = 1;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status = "1";

    public CategoryEntity toEntity() {
        CategoryEntity entity = new CategoryEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
