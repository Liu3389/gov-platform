package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_menu")
@Schema(description = "菜单信息")
public class MenuEntity extends BaseEntity {

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
}
