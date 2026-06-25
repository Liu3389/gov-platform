package com.gov.datashare.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.datashare.entity.ApiEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 共享接口入参 DTO
 */
@Data
@Schema(description = "共享接口请求")
public class ApiDTO {

    @Schema(description = "接口ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotBlank(message = "接口编码不能为空")
    @Schema(description = "接口编码", required = true, example = "USER_INFO_QUERY")
    private String apiCode = "USER_INFO_QUERY";

    @NotBlank(message = "接口名称不能为空")
    @Schema(description = "接口名称", required = true, example = "用户信息查询接口")
    private String apiName = "用户信息查询接口";

    @NotNull(message = "数据源ID不能为空")
    @Schema(description = "数据源ID", required = true, example = "1")
    private Long sourceId = 1L;

    @NotBlank(message = "接口URL不能为空")
    @Schema(description = "接口URL", required = true, example = "/api/v1/user/query")
    private String apiUrl = "/api/v1/user/query";

    @Schema(description = "请求方法", example = "GET")
    private String apiMethod = "GET";

    @Schema(description = "请求参数JSON", example = "{\"userId\": 1}")
    private String requestParams = "{\"userId\": 1}";

    @Schema(description = "响应参数JSON", example = "{\"code\": 200, \"data\": {}}")
    private String responseParams = "{\"code\": 200, \"data\": {}}";

    @Schema(description = "接口描述", example = "根据用户ID查询用户基本信息")
    private String apiDesc = "根据用户ID查询用户基本信息";

    @Schema(description = "认证方式", example = "token")
    private String authType = "token";

    @Schema(description = "超时时间（毫秒）", example = "5000")
    private Integer timeout = 5000;

    @Schema(description = "限流阀值", example = "100")
    private Integer rateLimit = 100;

    @Schema(description = "所属部门ID", example = "1")
    private Long deptId = 1L;

    @Schema(description = "所属部门名称", example = "数据管理局")
    private String deptName = "数据管理局";

    @Schema(description = "状态：1发布 0禁用", example = "1")
    private String status = "1";

    /** 转为 Entity，供 Service 层使用 */
    public ApiEntity toEntity() {
        ApiEntity entity = new ApiEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
