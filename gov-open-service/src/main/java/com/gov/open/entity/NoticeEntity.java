package com.gov.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告实体（示例）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_open_notice")
@Schema(description = "通知公告")
public class NoticeEntity extends BaseEntity {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "发布部门ID")
    private Long publishDeptId;

    @Schema(description = "发布时间")
    private java.time.LocalDateTime publishTime;

    @Schema(description = "状态：0草稿 1已发布 2已撤回")
    private Integer status;
}
