package com.gov.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "菜单信息响应")
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "菜单名称")
    private String menuName;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
