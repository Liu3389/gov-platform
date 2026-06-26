package com.gov.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 内容审核实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_open_content")
@Schema(description = "内容审核")
public class ContentEntity extends BaseEntity {

    @Schema(description = "内容类型：1通知公告 2政策法规 3依申请公开")
    private Integer contentType;

    @Schema(description = "内容ID")
    private Long contentId;

    @Schema(description = "审核状态：0待审核 1审核通过 2审核驳回")
    private Integer auditStatus;

    @Schema(description = "审核意见")
    private String auditOpinion;

    @Schema(description = "审核时间")
    private LocalDateTime auditTime;

    @Schema(description = "审核人ID")
    private Long auditBy;
}
