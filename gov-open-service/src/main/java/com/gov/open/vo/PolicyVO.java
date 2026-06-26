package com.gov.open.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 政策法规VO
 */
@Data
@Schema(description = "政策法规信息")
public class PolicyVO implements Serializable {

    @Schema(description = "政策ID")
    private Long id;

    @Schema(description = "政策编码")
    private String policyCode;

    @Schema(description = "政策名称")
    private String policyName;

    @Schema(description = "政策类型")
    private String policyType;

    @Schema(description = "政策类型描述")
    private String policyTypeDesc;

    @Schema(description = "发布部门ID")
    private Long publishDeptId;

    @Schema(description = "发布部门名称")
    private String publishDeptName;

    @Schema(description = "发布日期")
    private LocalDate publishDate;

    @Schema(description = "实施日期")
    private LocalDate implementDate;

    @Schema(description = "生效状态")
    private String effectiveStatus;

    @Schema(description = "政策内容")
    private String content;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "状态：0草稿 1已发布 2已废止")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
