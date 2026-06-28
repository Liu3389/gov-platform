package com.gov.monitor.dto;

import com.gov.monitor.entity.DashboardStatEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Schema(description = "数据大屏统计录入请求")
public class DashboardStatDTO {

    @NotBlank(message = "统计类型不能为空")
    @Schema(description = "统计类型：total/finish/timeout/satisfaction", required = true)
    private String statType;

    @NotNull(message = "统计值不能为空")
    @Schema(description = "统计值", required = true)
    private Long statValue;

    @NotNull(message = "统计日期不能为空")
    @Schema(description = "统计日期", required = true)
    private LocalDate statDate;

    @Schema(description = "统计小时")
    private Integer statHour;

    @Schema(description = "部门ID")
    private Long deptId;

    public DashboardStatEntity toEntity() {
        DashboardStatEntity entity = new DashboardStatEntity();
        entity.setStatType(this.statType);
        entity.setStatValue(this.statValue);
        entity.setStatDate(this.statDate);
        entity.setStatHour(this.statHour);
        entity.setDeptId(this.deptId);
        return entity;
    }
}
