package com.gov.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "部门信息响应")
public class DeptVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
