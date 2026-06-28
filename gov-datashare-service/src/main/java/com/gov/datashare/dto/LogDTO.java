package com.gov.datashare.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.datashare.entity.LogEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 交换日志入参 DTO
 */
@Data
@Schema(description = "交换日志请求")
public class LogDTO {

    @Schema(description = "日志ID（修改时必填）", example = "1")
    private Long id;

    @NotNull(message = "接口ID不能为空")
    @Schema(description = "接口ID", required = true, example = "1")
    private Long apiId = 1L;

    @Schema(description = "接口编码", example = "USER_INFO_QUERY")
    private String apiCode = "USER_INFO_QUERY";

    @Schema(description = "调用部门ID", example = "1")
    private Long callerDeptId = 1L;

    @Schema(description = "调用部门名称", example = "数据管理局")
    private String callerDeptName = "数据管理局";

    @Schema(description = "调用参数", example = "{\"userId\": 1}")
    private String callParams = "{\"userId\": 1}";

    @Schema(description = "调用结果", example = "success")
    private String callResult = "success";

    @Schema(description = "调用信息", example = "查询成功")
    private String callMsg = "查询成功";

    @Schema(description = "响应时间（毫秒）", example = "150")
    private Long responseTime = 150L;

    @Schema(description = "返回数据量", example = "1")
    private Integer dataCount = 1;

    @Schema(description = "调用IP", example = "192.168.1.100")
    private String callIp = "192.168.1.100";

    public LogEntity toEntity() {
        LogEntity entity = new LogEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
