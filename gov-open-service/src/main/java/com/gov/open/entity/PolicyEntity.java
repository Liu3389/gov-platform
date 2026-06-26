package com.gov.open.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 政策法规实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_open_policy")
@Schema(description = "政策法规")
public class PolicyEntity extends BaseEntity {

    @Schema(description = "政策编码")
    private String policyCode;

    @Schema(description = "政策名称")
    private String policyName;

    @Schema(description = "政策类型")
    private String policyType;

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
}
