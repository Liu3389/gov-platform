package com.gov.open.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 目录VO
 */
@Data
@Schema(description = "目录信息")
public class CatalogVO implements Serializable {

    @Schema(description = "目录ID")
    private Long id;

    @Schema(description = "目录编码")
    private String catalogCode;

    @Schema(description = "目录名称")
    private String catalogName;

    @Schema(description = "父目录ID")
    private Long parentId;

    @Schema(description = "目录级别")
    private Integer catalogLevel;

    @Schema(description = "目录类型")
    private String catalogType;

    @Schema(description = "目录类型描述")
    private String catalogTypeDesc;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0禁用 1启用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "子目录列表")
    private List<CatalogVO> children;
}
