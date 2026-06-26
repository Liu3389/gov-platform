package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 办件日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_reception_log")
@Schema(description = "办件日志")
public class ReceptionLogEntity extends BaseEntity {

    @Schema(description = "办件ID")
    private Long recordId;

    @Schema(description = "操作类型")
    private String actionType;

    @Schema(description = "操作时间")
    private LocalDateTime actionTime;

    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "操作人姓名")
    private String operatorName;

    @Schema(description = "备注")
    private String remark;
}
