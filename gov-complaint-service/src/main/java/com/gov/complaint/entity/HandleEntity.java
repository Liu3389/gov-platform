package com.gov.complaint.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 处理反馈实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_complaint_handle")
@Schema(description = "处理反馈")
public class HandleEntity extends BaseEntity {

    @Schema(description = "工单ID")
    private Long workId;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "处理人姓名")
    private String handlerName;

    @Schema(description = "处理类型")
    private String handleType;

    @Schema(description = "处理内容")
    private String handleContent;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "下一部门ID")
    private Long nextDeptId;

    @Schema(description = "下一部门名称")
    private String nextDeptName;
}
