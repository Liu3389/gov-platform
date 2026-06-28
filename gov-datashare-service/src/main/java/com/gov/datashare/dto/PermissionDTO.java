package com.gov.datashare.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.datashare.entity.PermissionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 接口权限入参 DTO
 */
@Data
@Schema(description = "接口权限请求")
public class PermissionDTO {

    @Schema(description = "权限ID（修改时必填）", example = "1")
    private Long id;

    @NotNull(message = "接口ID不能为空")
    @Schema(description = "接口ID", required = true, example = "1")
    private Long apiId = 1L;

    @NotNull(message = "申请部门ID不能为空")
    @Schema(description = "申请部门ID", required = true, example = "1")
    private Long deptId = 1L;

    @Schema(description = "申请部门名称", example = "数据管理局")
    private String deptName = "数据管理局";

    @Schema(description = "权限类型", example = "read")
    private String permissionType = "read";

    @Schema(description = "权限过期时间", example = "2026-12-31T23:59:59")
    private LocalDateTime expireTime = LocalDateTime.of(2026, 12, 31, 23, 59, 59);

    @Schema(description = "调用次数限制", example = "1000")
    private Integer callLimit = 1000;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status = "1";

    public PermissionEntity toEntity() {
        PermissionEntity entity = new PermissionEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
