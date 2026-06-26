package com.gov.open.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知公告VO
 */
@Data
@Schema(description = "通知公告信息")
public class NoticeVO implements Serializable {

    @Schema(description = "公告ID")
    private Long id;

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

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
