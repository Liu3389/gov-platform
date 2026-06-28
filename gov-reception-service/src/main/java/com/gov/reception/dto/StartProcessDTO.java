package com.gov.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 启动流程DTO
 */
@Data
@Schema(description = "启动审批流程请求")
public class StartProcessDTO {

    @NotBlank(message = "流程Key不能为空")
    @Schema(description = "流程Key", required = true)
    private String processKey;

    @NotBlank(message = "办件号不能为空")
    @Schema(description = "办件号", required = true)
    private String applyNo;

    @NotNull(message = "申请人ID不能为空")
    @Schema(description = "申请人ID", required = true)
    private Long userId;

    @NotNull(message = "受理部门ID不能为空")
    @Schema(description = "受理部门ID", required = true)
    private Long deptId;

    @NotNull(message = "事项ID不能为空")
    @Schema(description = "事项ID", required = true)
    private Long itemId;

    @NotBlank(message = "事项名称不能为空")
    @Schema(description = "事项名称", required = true)
    private String itemName;

    @NotBlank(message = "申请人姓名不能为空")
    @Schema(description = "申请人姓名", required = true)
    private String userName;
}
