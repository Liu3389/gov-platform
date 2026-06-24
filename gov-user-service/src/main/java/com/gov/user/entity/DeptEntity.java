package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 部门信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_dept_info")
@Schema(description = "部门信息")
public class DeptEntity extends BaseEntity {

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "部门编码")
    private String deptCode;

    @Schema(description = "上级部门ID")
    private Long parentId;

    @Schema(description = "部门类型：1行政机关 2事业单位 3企业")
    private Integer deptType;

    @Schema(description = "负责人ID")
    private Long leaderId;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "办公地址")
    private String address;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;
}
