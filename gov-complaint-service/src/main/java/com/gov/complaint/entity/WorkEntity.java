package com.gov.complaint.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 投诉工单实体（示例）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_complaint_work")
@Schema(description = "投诉工单")
public class WorkEntity extends BaseEntity {

    @Schema(description = "工单号")
    private String workNo;

    @Schema(description = "投诉人ID")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "状态：0待分办 1已分办 2处理中 3已回复 4已结案")
    private Integer status;
}
