package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_role_info")
@Schema(description = "角色信息")
public class RoleEntity extends BaseEntity {

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
