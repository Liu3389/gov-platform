package com.gov.datashare.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.datashare.entity.DataSourceEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据源入参 DTO
 */
@Data
@Schema(description = "数据源请求")
public class DataSourceDTO {

    @Schema(description = "数据源ID（修改时必填）", example = "1")
    private Long id;

    @NotBlank(message = "数据源编码不能为空")
    @Schema(description = "数据源编码", required = true, example = "DS_USER_DB")
    private String sourceCode = "DS_USER_DB";

    @NotBlank(message = "数据源名称不能为空")
    @Schema(description = "数据源名称", required = true, example = "用户数据库")
    private String sourceName = "用户数据库";

    @NotBlank(message = "数据源类型不能为空")
    @Schema(description = "数据源类型", required = true, example = "MySQL")
    private String sourceType = "MySQL";

    @Schema(description = "数据库主机", example = "127.0.0.1")
    private String dbHost = "127.0.0.1";

    @Schema(description = "数据库端口", example = "3307")
    private Integer dbPort = 3307;

    @Schema(description = "数据库名称", example = "gov_user")
    private String dbName = "gov_user";

    @Schema(description = "数据库用户名[脱敏]", example = "root")
    private String dbUsername = "root";

    @Schema(description = "数据库密码[脱敏]", example = "******")
    private String dbPassword = "******";

    @Schema(description = "接口URL", example = "https://api.example.com/data")
    private String apiUrl = "https://api.example.com/data";

    @Schema(description = "请求方法", example = "GET")
    private String apiMethod = "GET";

    @Schema(description = "接口密钥[脱敏]", example = "******")
    private String apiKey = "******";

    @NotNull(message = "所属部门ID不能为空")
    @Schema(description = "所属部门ID", required = true, example = "1")
    private Long deptId = 1L;

    @Schema(description = "所属部门名称", example = "数据管理局")
    private String deptName = "数据管理局";

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status = "1";

    public DataSourceEntity toEntity() {
        DataSourceEntity entity = new DataSourceEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
