package com.gov.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志实体（示例）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_operate_log")
@Schema(description = "操作日志")
public class LogEntity extends BaseEntity {

    @Schema(description = "操作人ID")
    private Long userId;

    @Schema(description = "操作人姓名")
    private String userName;

    @Schema(description = "操作模块")
    private String module;

    @Schema(description = "操作动作")
    private String action;

    @Schema(description = "操作时间")
    private java.time.LocalDateTime operateTime;
}
