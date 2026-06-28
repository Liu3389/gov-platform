package com.gov.datashare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 交换日志实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_share_log")
@Schema(description = "交换日志")
public class LogEntity extends BaseEntity {

    @Schema(description = "接口ID")
    private Long apiId;

    @Schema(description = "接口编码")
    private String apiCode;

    @Schema(description = "调用部门ID")
    private Long callerDeptId;

    @Schema(description = "调用部门名称")
    private String callerDeptName;

    @Schema(description = "调用人ID")
    private Long callerUserId;

    @Schema(description = "调用时间")
    private LocalDateTime callTime;

    @Schema(description = "调用参数")
    private String callParams;

    @Schema(description = "调用结果")
    private String callResult;

    @Schema(description = "调用信息")
    private String callMsg;

    @Schema(description = "响应时间（毫秒）")
    private Long responseTime;

    @Schema(description = "返回数据量")
    private Integer dataCount;

    @Schema(description = "调用IP")
    private String callIp;
}
