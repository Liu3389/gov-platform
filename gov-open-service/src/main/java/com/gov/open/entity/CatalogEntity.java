package com.gov.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 公开目录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_open_catalog")
@Schema(description = "公开目录")
public class CatalogEntity extends BaseEntity {

    @Schema(description = "目录编码")
    private String catalogCode;

    @Schema(description = "目录名称")
    private String catalogName;

    @Schema(description = "父目录ID（0为顶级）")
    private Long parentId;

    @Schema(description = "目录级别")
    private Integer catalogLevel;

    @Schema(description = "目录类型")
    private String catalogType;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0禁用 1启用")
    private String status;
}
