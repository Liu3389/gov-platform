package com.gov.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通知公告实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_open_notice")
@Schema(description = "通知公告")
public class NoticeEntity extends BaseEntity {

    @Schema(description = "公告编号")
    private String noticeCode;

    @Schema(description = "公告标题")
    private String title;

    @Schema(description = "公告内容")
    private String content;

    @Schema(description = "公告类型")
    private String noticeType;

    @Schema(description = "发布部门ID")
    private Long publishDeptId;

    @Schema(description = "发布部门名称")
    private String publishDeptName;

    @Schema(description = "发布人ID")
    private Long publishUserId;

    @Schema(description = "发布人姓名")
    private String publishUserName;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "是否置顶：0否 1是")
    private Integer topFlag;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "状态：0草稿 1已发布 2已撤回")
    private String status;
}
