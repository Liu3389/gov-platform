package com.gov.open.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 依申请公开DTO
 */
@Data
@Schema(description = "依申请公开请求")
public class ApplyDTO {

    @NotBlank(message = "申请内容不能为空")
    @Schema(description = "申请内容", required = true)
    private String applyContent;

    @Schema(description = "申请原因")
    private String applyReason;

    @Schema(description = "申请方式")
    private String applyType;

    @Schema(description = "部门ID")
    private Long deptId;
}
