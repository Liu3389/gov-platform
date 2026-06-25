package com.gov.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "行政区划请求")
public class RegionDTO {

    @Schema(description = "区划ID（修改时必填）")
    private Long id;

    @NotBlank(message = "区划编码不能为空")
    @Schema(description = "区划编码")
    private String regionCode;

    @NotBlank(message = "区划名称不能为空")
    @Schema(description = "区划名称")
    private String regionName;

    @Schema(description = "上级区划编码")
    private String parentCode;

    @Schema(description = "区划级别：1省 2市 3县 4乡镇")
    private Integer regionLevel;

    @NotNull(message = "排序号不能为空")
    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;
}
