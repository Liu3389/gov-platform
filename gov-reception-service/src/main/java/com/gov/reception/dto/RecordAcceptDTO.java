package com.gov.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 受理登记DTO
 */
@Data
@Schema(description = "受理登记请求")
public class RecordAcceptDTO {

    @NotNull(message = "办件ID不能为空")
    @Schema(description = "办件ID", required = true)
    private Long recordId;

    @NotNull(message = "受理结果不能为空")
    @Schema(description = "受理结果：1通过 2驳回", required = true)
    private Integer acceptResult;

    @Schema(description = "窗口ID")
    private Long windowId;

    @Schema(description = "驳回原因（受理驳回时必填）")
    private String rejectReason;

    @Schema(description = "备注")
    private String remark;

    /**
     * 转为 Entity
     */
    public com.gov.reception.entity.RecordEntity toEntity() {
        com.gov.reception.entity.RecordEntity entity = new com.gov.reception.entity.RecordEntity();
        entity.setId(this.recordId);
        entity.setWindowId(this.windowId);
        entity.setRejectReason(this.rejectReason);
        return entity;
    }
}
