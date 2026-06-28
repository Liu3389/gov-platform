package com.gov.datashare.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订阅记录出参 VO
 */
@Data
@Schema(description = "订阅记录信息")
public class SubscribeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订阅ID")
    private Long id;

    @Schema(description = "接口ID")
    private Long apiId;

    @Schema(description = "接口名称")
    private String apiName;

    @Schema(description = "订阅部门ID")
    private Long subscribeDeptId;

    @Schema(description = "订阅部门名称")
    private String subscribeDeptName;

    @Schema(description = "订阅人ID")
    private Long subscribeUserId;

    @Schema(description = "订阅人姓名")
    private String subscribeUserName;

    @Schema(description = "订阅时间")
    private LocalDateTime subscribeTime;

    @Schema(description = "订阅类型")
    private String subscribeType;

    @Schema(description = "同步间隔（分钟）")
    private Integer syncInterval;

    @Schema(description = "最后同步时间")
    private LocalDateTime lastSyncTime;

    @Schema(description = "状态：1启用 0禁用")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
