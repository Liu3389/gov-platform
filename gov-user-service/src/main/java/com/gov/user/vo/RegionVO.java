package com.gov.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "行政区划响应")
public class RegionVO implements Serializable {

    @Schema(description = "区划ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
