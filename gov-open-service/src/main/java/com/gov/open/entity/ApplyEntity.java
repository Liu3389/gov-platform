package com.gov.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 依申请公开实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_open_apply")
@Schema(description = "依申请公开")
public class ApplyEntity extends BaseEntity {

    @Schema(description = "申请编号")
    private String applyNo;

    @Schema(description = "申请人ID")
    private Long userId;

    @Schema(description = "申请人姓名")
    private String userName;

    @Schema(description = "申请人手机号")
    private String userPhone;

    @Schema(description = "申请人身份证号")
    private String userIdCard;

    @Schema(description = "申请内容")
    private String applyContent;

    @Schema(description = "申请原因")
    private String applyReason;

    @Schema(description = "不予公开原因")
    private String rejectReason;

    @Schema(description = "申请方式")
    private String applyType;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "状态：0待处理 1已受理 2已答复 3不予公开")
    private String status;

    @Schema(description = "申请时间")
    private LocalDateTime applyTime;

    @Schema(description = "受理时间")
    private LocalDateTime acceptTime;

    @Schema(description = "答复时间")
    private LocalDateTime replyTime;

    @Schema(description = "答复内容")
    private String replyContent;

    @Schema(description = "答复类型")
    private String replyType;

    @Schema(description = "是否收费：0否 1是")
    private Integer feeFlag;

    @Schema(description = "收费金额")
    private BigDecimal feeAmount;
}
