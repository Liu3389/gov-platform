package com.gov.complaint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "投诉分类信息")
public class CategoryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "分类ID") private Long id;
    @Schema(description = "分类名称") private String categoryName;
    @Schema(description = "分类编码") private String categoryCode;
    @Schema(description = "父级ID") private Long parentId;
    @Schema(description = "关键词") private String keywords;
    @Schema(description = "默认处理部门ID") private Long defaultDeptId;
    @Schema(description = "排序") private Integer sort;
    @Schema(description = "状态：1启用 0禁用") private String status;
    @Schema(description = "创建时间") private LocalDateTime createTime;
    @Schema(description = "更新时间") private LocalDateTime updateTime;
}
