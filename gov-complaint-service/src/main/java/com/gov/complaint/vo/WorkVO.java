package com.gov.complaint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 投诉工单出参 VO
 */
@Data
@Schema(description = "投诉工单信息")
public class WorkVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "工单ID")
    private Long id;

    @Schema(description = "工单编号")
    private String workNo;

    @Schema(description = "投诉人ID")
    private Long userId;

    @Schema(description = "投诉人姓名")
    private String userName;

    @Schema(description = "投诉人电话 [脱敏]")
    private String userPhone;

    @Schema(description = "投诉人身份证号 [脱敏]")
    private String userIdCard;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "投诉类型")
    private String complaintType;

    @Schema(description = "投诉标题")
    private String title;

    @Schema(description = "投诉内容")
    private String content;

    @Schema(description = "图片附件")
    private String images;

    @Schema(description = "处理部门ID")
    private Long deptId;

    @Schema(description = "处理部门名称")
    private String deptName;

    @Schema(description = "处理人ID")
    private Long handlerId;

    @Schema(description = "处理人姓名")
    private String handlerName;

    @Schema(description = "工单状态：0待分办 1已分办 2处理中 3已回复 4已结案")
    private String status;

    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    @Schema(description = "分配时间")
    private LocalDateTime assignTime;

    @Schema(description = "处理时间")
    private LocalDateTime handleTime;

    @Schema(description = "办结时间")
    private LocalDateTime finishTime;

    @Schema(description = "满意度评分")
    private Integer satisfaction;

    @Schema(description = "满意度评价时间")
    private LocalDateTime satisfactionTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
