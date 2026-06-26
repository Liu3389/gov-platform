package com.gov.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 窗口DTO
 */
@Data
@Schema(description = "窗口请求")
public class WindowDTO {

    @NotBlank(message = "窗口编号不能为空")
    @Schema(description = "窗口编号", required = true)
    private String windowNo;

    @NotBlank(message = "窗口名称不能为空")
    @Schema(description = "窗口名称", required = true)
    private String windowName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "窗口工作人员ID")
    private Long staffId;

    @Schema(description = "状态：0关闭 1开放 2暂停")
    private String status;
}
