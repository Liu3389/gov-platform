package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 行政区划实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_region")
@Schema(description = "行政区划")
public class RegionEntity extends BaseEntity {

    @Schema(description = "区划编码")
    private String regionCode;

    @Schema(description = "区划名称")
    private String regionName;

    @Schema(description = "上级区划编码")
    private String parentCode;

    @Schema(description = "区划级别：1省 2市 3县 4乡镇")
    private Integer regionLevel;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;
}
