package com.gov.license.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 证照数据实体（示例）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_license_data")
@Schema(description = "证照数据")
public class LicenseEntity extends BaseEntity {

    @Schema(description = "证照编号")
    private String licenseNo;

    @Schema(description = "证照目录ID")
    private Long catalogId;

    @Schema(description = "持证人ID")
    private Long userId;

    @Schema(description = "关联办件号")
    private String applyNo;

    @Schema(description = "状态：1有效 2过期 3注销")
    private Integer status;
}
