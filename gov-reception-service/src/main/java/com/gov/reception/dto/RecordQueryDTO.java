package com.gov.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

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
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    /**
     * 转为 Entity
     */
    public com.gov.reception.entity.RecordEntity toEntity() {
        com.gov.reception.entity.RecordEntity entity = new com.gov.reception.entity.RecordEntity();
        entity.setApplyNo(this.applyNo);
        entity.setItemId(this.itemId);
        entity.setUserId(this.userId);
        entity.setDeptId(this.deptId);
        entity.setStatus(this.status);
        return entity;
    }
}
