package com.gov.datashare.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.datashare.entity.CatalogEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 共享目录入参 DTO
 */
@Data
@Schema(description = "共享目录请求")
public class CatalogDTO {

    @Schema(description = "目录ID（修改时必填）", example = "1")
    private Long id;

    @NotBlank(message = "目录编码不能为空")
    @Schema(description = "目录编码", required = true, example = "CAT_USER")
    private String catalogCode = "CAT_USER";

    @NotBlank(message = "目录名称不能为空")
    @Schema(description = "目录名称", required = true, example = "用户数据目录")
    private String catalogName = "用户数据目录";

    @Schema(description = "父级ID", example = "0")
    private Long parentId = 0L;

    @Schema(description = "目录层级", example = "1")
    private Integer catalogLevel = 1;

    @Schema(description = "数据类型", example = "结构化数据")
    private String dataType = "结构化数据";

    @Schema(description = "数据量", example = "10000")
    private Integer dataCount = 10000;

    @NotNull(message = "所属部门ID不能为空")
    @Schema(description = "所属部门ID", required = true, example = "1")
    private Long deptId = 1L;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status = "1";

    public CatalogEntity toEntity() {
        CatalogEntity entity = new CatalogEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
