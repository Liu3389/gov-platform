package com.gov.reception.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 办件详情VO
 */
@Data
@Schema(description = "办件详情")
public class RecordVO implements Serializable {

    @Schema(description = "办件ID")
    private Long id;

    @Schema(description = "办件号")
    private String applyNo;

    @Schema(description = "事项ID")
    private Long itemId;

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "申请人ID")
    private Long userId;

    @Schema(description = "申请人姓名")
    private String userName;

    @Schema(description = "受理部门ID")
    private Long deptId;

    @Schema(description = "受理部门名称")
    private String deptName;

    @Schema(description = "窗口ID")
    private Long windowId;

    @Schema(description = "窗口名称")
    private String windowName;

    @Schema(description = "受理人ID")
    private Long operatorId;

    @Schema(description = "受理人姓名")
    private String operatorName;

    @Schema(description = "状态：0待受理 1受理中 2审批中 3办结 4驳回")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "申报材料列表")
    private List<MaterialVO> materials;
}
