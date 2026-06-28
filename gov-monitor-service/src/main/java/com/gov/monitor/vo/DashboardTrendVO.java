package com.gov.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Schema(description = "数据大屏趋势")
public class DashboardTrendVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Schema(description = "日办件量")
    private Long dailyCount;
}
