package com.gov.datashare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 交换日志出参 VO
 */
@Data
@Schema(description = "交换日志信息")
public class LogVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "日志ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
