package com.gov.datashare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 共享目录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_share_catalog")
@Schema(description = "共享目录")
public class CatalogEntity extends BaseEntity {

    @Schema(description = "目录编码")
    private String catalogCode;

    @Schema(description = "目录名称")
    private String catalogName;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "目录层级")
    private Integer catalogLevel;

    @Schema(description = "数据类型")
    private String dataType;

    @Schema(description = "数据量")
    private Integer dataCount;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "状态：1启用 0禁用")
    private String status;
}
