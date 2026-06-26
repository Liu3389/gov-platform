package com.gov.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 办件查询DTO
 */
@Data
@Schema(description = "办件查询请求")
public class RecordQueryDTO {

    @Schema(description = "办件号")
    private String applyNo;

    @Schema(description = "事项ID")
    private Long itemId;

    @Schema(description = "申请人ID")
    private Long userId;

    @Schema(description = "受理部门ID")
    private Long deptId;

    @Schema(description = "状态：0待受理 1受理中 2审批中 3办结 4驳回")
    private String status;

    @Schema(description = "开始时间")
    private String startTime;

    @Schema(description = "结束时间")
    private String endTime;
}
