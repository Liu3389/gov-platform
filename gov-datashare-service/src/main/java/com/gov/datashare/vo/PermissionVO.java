package com.gov.datashare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 接口权限出参 VO
 */
@Data
@Schema(description = "接口权限信息")
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "权限ID")
    private Long id;

    @Schema(description = "接口ID")
    private Long apiId;

    @Schema(description = "申请部门ID")
    private Long deptId;

    @Schema(description = "申请部门名称")
    private String deptName;

    @Schema(description = "权限类型")
    private String permissionType;

    @Schema(description = "权限过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "调用次数限制")
    private Integer callLimit;

    @Schema(description = "状态：1启用 0禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
