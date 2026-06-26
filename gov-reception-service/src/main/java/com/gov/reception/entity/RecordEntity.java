package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 办件实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_reception_record")
@Schema(description = "办件信息")
public class RecordEntity extends BaseEntity {

    @Schema(description = "办件号")
    private String applyNo;

    @Schema(description = "事项ID")
    private Long itemId;

    @Schema(description = "申请人ID")
    private Long userId;

    @Schema(description = "受理部门ID")
    private Long deptId;

    @Schema(description = "受理窗口ID")
    private Long windowId;

    @Schema(description = "受理人ID")
    private Long operatorId;

    @Schema(description = "状态：0待受理 1受理中 2审批中 3办结 4驳回")
    private String status;

    @Schema(description = "受理时间")
    private LocalDateTime acceptTime;

    @Schema(description = "办结时间")
    private LocalDateTime finishTime;

    @Schema(description = "流程实例ID")
    private String processInstanceId;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "驳回原因")
    private String rejectReason;
}
