package com.gov.datashare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据源实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_datasource")
@Schema(description = "数据源")
public class DataSourceEntity extends BaseEntity {

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

    @Schema(description = "数据库用户名[脱敏][展示掩码]")
    private String dbUsername;

    @Schema(description = "数据库密码[脱敏][加密存储]")
    private String dbPassword;

    @Schema(description = "接口URL")
    private String apiUrl;

    @Schema(description = "请求方法")
    private String apiMethod;

    @Schema(description = "接口密钥[脱敏][加密存储]")
    private String apiKey;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "所属部门名称")
    private String deptName;

    @Schema(description = "状态：1启用 0禁用")
    private String status;
}
