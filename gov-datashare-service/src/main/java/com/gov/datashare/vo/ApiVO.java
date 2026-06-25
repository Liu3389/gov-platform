package com.gov.datashare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 共享接口出参 VO
 */
@Data
@Schema(description = "共享接口信息")
public class ApiVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "接口ID")
    private Long id;

    @Schema(description = "接口编码")
    private String apiCode;

    @Schema(description = "接口名称")
    private String apiName;

    @Schema(description = "数据源ID")
    private Long sourceId;

    @Schema(description = "接口URL")
    private String apiUrl;

    @Schema(description = "请求方法")
    private String apiMethod;

    @Schema(description = "请求参数JSON")
    private String requestParams;

    @Schema(description = "响应参数JSON")
    private String responseParams;

    @Schema(description = "接口描述")
    private String apiDesc;

    @Schema(description = "认证方式")
    private String authType;

    @Schema(description = "超时时间（毫秒）")
    private Integer timeout;

    @Schema(description = "限流阀值")
    private Integer rateLimit;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "所属部门名称")
    private String deptName;

    @Schema(description = "状态：1发布 0禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
