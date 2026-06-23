package com.gov.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 证照 VO（Feign 远程调用返回）
 */
@Data
@Schema(description = "证照信息")
public class LicenseVO implements Serializable {

    @Schema(description = "ID")
    private Long id;

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

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
