package com.gov.monitor.dto;

import com.gov.monitor.entity.AuditReportEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "审计报告请求")
public class AuditReportDTO {

    @NotBlank(message = "报告编号不能为空")
    @Schema(description = "报告编号", required = true)
    private String reportNo;

    @NotBlank(message = "报告名称不能为空")
    @Schema(description = "报告名称", required = true)
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

    public AuditReportEntity toEntity() {
        AuditReportEntity entity = new AuditReportEntity();
        entity.setReportNo(this.reportNo);
        entity.setReportName(this.reportName);
        entity.setReportType(this.reportType);
        entity.setReportDate(this.reportDate);
        entity.setReportContent(this.reportContent);
        entity.setFileUrl(this.fileUrl);
        entity.setGenerateTime(this.generateTime);
        entity.setGenerateBy(this.generateBy);
        entity.setStatus(this.status);
        return entity;
    }
}
