package com.gov.datashare.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 共享接口实体（示例）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_share_api")
@Schema(description = "共享接口")
public class ApiEntity extends BaseEntity {

    @Schema(description = "接口编码")
    private String apiCode;

    @Schema(description = "接口名称")
    private String apiName;

    @Schema(description = "接口URL")
    private String apiUrl;

    @Schema(description = "状态：1发布 0禁用")
    private Integer status;
}
