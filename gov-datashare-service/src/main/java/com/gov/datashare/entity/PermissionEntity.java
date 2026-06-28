package com.gov.datashare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 接口权限实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_share_permission")
@Schema(description = "接口权限")
public class PermissionEntity extends BaseEntity {

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
}
