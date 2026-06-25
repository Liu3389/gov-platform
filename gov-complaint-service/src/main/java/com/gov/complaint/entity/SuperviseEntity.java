package com.gov.complaint.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 督办记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_complaint_supervise")
@Schema(description = "督办记录")
public class SuperviseEntity extends BaseEntity {

    @Schema(description = "工单ID")
    private Long workId;

    @Schema(description = "督办级别")
    private String superviseLevel;

    @Schema(description = "督办时间")
    private LocalDateTime superviseTime;

    @Schema(description = "督办人ID")
    private Long superviseBy;

    @Schema(description = "督办内容")
    private String superviseContent;

    @Schema(description = "办理期限")
    private LocalDateTime deadline;

    @Schema(description = "状态")
    private String status;
}
