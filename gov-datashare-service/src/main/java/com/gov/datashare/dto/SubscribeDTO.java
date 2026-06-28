package com.gov.datashare.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.datashare.entity.SubscribeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 订阅记录入参 DTO
 */
@Data
@Schema(description = "订阅记录请求")
public class SubscribeDTO {

    @Schema(description = "订阅ID（修改时必填）", example = "1")
    private Long id;

    @NotNull(message = "接口ID不能为空")
    @Schema(description = "接口ID", required = true, example = "1")
    private Long apiId = 1L;

    @Schema(description = "接口名称", example = "用户信息查询接口")
    private String apiName = "用户信息查询接口";

    @NotNull(message = "订阅部门ID不能为空")
    @Schema(description = "订阅部门ID", required = true, example = "1")
    private Long subscribeDeptId = 1L;

    @Schema(description = "订阅部门名称", example = "数据管理局")
    private String subscribeDeptName = "数据管理局";

    @Schema(description = "订阅类型", example = "realtime")
    private String subscribeType = "realtime";

    @Schema(description = "同步间隔（分钟）", example = "60")
    private Integer syncInterval = 60;

    @Schema(description = "状态：1启用 0禁用", example = "1")
    private String status = "1";

    public SubscribeEntity toEntity() {
        SubscribeEntity entity = new SubscribeEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
