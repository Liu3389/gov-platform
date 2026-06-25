package com.gov.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "部门信息请求")
public class DeptDTO {

    @Schema(description = "部门ID（修改时必填）")
    private Long id;

    @NotBlank(message = "部门名称不能为空")
    @Size(max = 100, message = "部门名称长度不能超过100")
    @Schema(description = "部门名称")
    private String deptName;

    @NotBlank(message = "部门编码不能为空")
    @Size(max = 50, message = "部门编码长度不能超过50")
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
