package com.gov.datashare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 共享目录出参 VO
 */
@Data
@Schema(description = "共享目录信息")
public class CatalogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "目录ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
