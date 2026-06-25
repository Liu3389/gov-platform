package com.gov.complaint.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投诉分类实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_complaint_category")
@Schema(description = "投诉分类")
public class CategoryEntity extends BaseEntity {

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "分类编码")
    private String categoryCode;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(description = "默认处理部门ID")
    private Long defaultDeptId;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：1启用 0禁用")
    private String status;
}
