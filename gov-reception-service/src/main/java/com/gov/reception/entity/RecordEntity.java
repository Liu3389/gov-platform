package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 办件实体（示例）
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

    @Schema(description = "状态：0待受理 1受理中 2审批中 3办结 4驳回")
    private Integer status;
}
