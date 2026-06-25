package com.gov.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "角色信息请求")
public class RoleDTO {

    @Schema(description = "角色ID（修改时必填）")
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50")
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50")
    @Schema(description = "角色编码")
    private String roleCode;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
