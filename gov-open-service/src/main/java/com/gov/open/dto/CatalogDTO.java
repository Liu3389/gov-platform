package com.gov.open.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 目录DTO
 */
@Data
@Schema(description = "目录请求")
public class CatalogDTO {

    @NotBlank(message = "目录编码不能为空")
    @Schema(description = "目录编码", required = true)
    private String catalogCode;

    @NotBlank(message = "目录名称不能为空")
    @Schema(description = "目录名称", required = true)
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

    /**
     * 转为 Entity
     */
    public com.gov.open.entity.CatalogEntity toEntity() {
        com.gov.open.entity.CatalogEntity entity = new com.gov.open.entity.CatalogEntity();
        entity.setCatalogCode(this.catalogCode);
        entity.setCatalogName(this.catalogName);
        entity.setParentId(this.parentId);
        entity.setCatalogLevel(this.catalogLevel);
        entity.setCatalogType(this.catalogType);
        entity.setSort(this.sort);
        entity.setStatus(this.status);
        return entity;
    }
}
