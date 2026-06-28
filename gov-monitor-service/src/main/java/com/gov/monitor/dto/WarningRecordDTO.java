package com.gov.monitor.dto;

import com.gov.monitor.entity.WarningRecordEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Schema(description = "预警记录录入/处理请求")
public class WarningRecordDTO {

    @NotBlank(message = "办件号不能为空")
    @Schema(description = "办件号", required = true)
    private String applyNo;

    @Schema(description = "事项ID")
    private Long itemId;

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @NotBlank(message = "预警类型不能为空")
    @Schema(description = "预警类型：YELLOW_CARD/RED_CARD", required = true)
    private String warningType;

    @Schema(description = "预警级别：1黄牌 2红牌")
    private Integer warningLevel;

    @Schema(description = "预警时间")
    private LocalDateTime warningTime;

    @Schema(description = "预警原因")
    private String warningReason;

    @Schema(description = "剩余时间（分钟）")
    private Integer remainingTime;

    @Schema(description = "处理状态：0待处理 1已处理")
    private Integer handleStatus;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "处理人ID")
    private Long handleBy;

    @Schema(description = "处理备注")
    private String handleRemark;

    public WarningRecordEntity toEntity() {
        WarningRecordEntity entity = new WarningRecordEntity();
        entity.setApplyNo(this.applyNo);
        entity.setItemId(this.itemId);
        entity.setItemName(this.itemName);
        entity.setDeptId(this.deptId);
        entity.setDeptName(this.deptName);
        entity.setWarningType(this.warningType);
        entity.setWarningLevel(this.warningLevel);
        entity.setWarningTime(this.warningTime);
        entity.setWarningReason(this.warningReason);
        entity.setRemainingTime(this.remainingTime);
        entity.setHandleStatus(this.handleStatus);
        entity.setHandleTime(this.handleTime);
        entity.setHandleBy(this.handleBy);
        entity.setHandleRemark(this.handleRemark);
        return entity;
    }
}
