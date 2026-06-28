package com.gov.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_warning_record")
@Schema(description = "红黄牌预警")
public class WarningRecordEntity extends BaseEntity {

    @TableField(exist = false)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Long createBy;

    @TableField(exist = false)
    private Long updateBy;

    @Schema(description = "办件号")
    private String applyNo;

    @Schema(description = "事项ID")
    private Long itemId;

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "预警类型：YELLOW_CARD/RED_CARD")
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
}
