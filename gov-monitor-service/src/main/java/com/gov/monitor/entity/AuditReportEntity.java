package com.gov.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_audit_report")
@Schema(description = "审计报告")
public class AuditReportEntity extends BaseEntity {

    @TableField(exist = false)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private Long createBy;

    @TableField(exist = false)
    private Long updateBy;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "报告名称")
    private String reportName;

    @Schema(description = "报告类型：1日报 2周报 3月报 4年报")
    private Integer reportType;

    @Schema(description = "报告日期")
    private LocalDate reportDate;

    @Schema(description = "报告内容（JSON格式）")
    private String reportContent;

    @Schema(description = "报告文件URL")
    private String fileUrl;

    @Schema(description = "生成时间")
    private LocalDateTime generateTime;

    @Schema(description = "生成人ID")
    private Long generateBy;

    @Schema(description = "状态：1已生成 0待生成")
    private Integer status;
}
