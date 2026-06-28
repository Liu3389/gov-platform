package com.gov.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 证照 VO（Feign 远程调用返回）
 * 字段对齐 com.gov.license.vo.LicenseVO
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

    @Schema(description = "证照目录名称")
    private String catalogName;

    @Schema(description = "持证人ID")
    private Long userId;

    @Schema(description = "持证人姓名")
    private String userName;

    @Schema(description = "关联办件号")
    private String applyNo;

    @Schema(description = "证照文件地址")
    private String fileUrl;

    @Schema(description = "签章时间")
    private LocalDateTime signTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "证照状态：1有效 2过期 3注销")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
