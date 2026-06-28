package com.gov.datashare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据源出参 VO
 */
@Data
@Schema(description = "数据源信息")
public class DataSourceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "数据源ID")
    private Long id;

    @Schema(description = "数据源编码")
    private String sourceCode;

    @Schema(description = "数据源名称")
    private String sourceName;

    @Schema(description = "数据源类型")
    private String sourceType;

    @Schema(description = "数据库主机")
    private String dbHost;

    @Schema(description = "数据库端口")
    private Integer dbPort;

    @Schema(description = "数据库名称")
    private String dbName;

    @Schema(description = "数据库用户名[脱敏]")
    private String dbUsername;

    @Schema(description = "数据库密码[脱敏]")
    private String dbPassword;

    @Schema(description = "接口URL")
    private String apiUrl;

    @Schema(description = "请求方法")
    private String apiMethod;

    @Schema(description = "接口密钥[脱敏]")
    private String apiKey;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "所属部门名称")
    private String deptName;

    @Schema(description = "状态：1启用 0禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
