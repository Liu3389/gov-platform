package com.gov.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "数据大屏概览")
public class DashboardOverviewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "今日办件数")
    private Long todayTotal;

    @Schema(description = "在办数")
    private Long inProgress;

    @Schema(description = "办结数")
    private Long completed;

    @Schema(description = "满意度")
    private BigDecimal satisfactionRate;
}
