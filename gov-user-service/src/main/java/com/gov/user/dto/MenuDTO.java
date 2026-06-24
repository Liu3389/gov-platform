package com.gov.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "菜单信息请求")
public class MenuDTO {

    @Schema(description = "菜单ID（修改时必填）")
    private Long id;

    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 50, message = "菜单名称长度不能超过50")
    @Schema(description = "菜单名称")
    private String menuName;

    @NotBlank(message = "菜单编码不能为空")
    @Size(max = 50, message = "菜单编码长度不能超过50")
    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "菜单路径")
    private String menuUrl;

    @Schema(description = "菜单类型：1目录 2菜单 3按钮")
    private Integer menuType;

    @Schema(description = "上级菜单ID")
    private Long parentId;

    @Schema(description = "菜单图标")
    private String icon;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否可见：1显示 0隐藏")
    private Integer visible;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;
}
