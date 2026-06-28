package com.gov.monitor.dto;

import com.gov.monitor.entity.EfficiencyStatEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "效能统计数据录入请求")
public class EfficiencyStatDTO {

    @NotNull(message = "部门ID不能为空")
    @Schema(description = "部门ID", required = true)
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @NotNull(message = "统计日期不能为空")
    @Schema(description = "统计日期", required = true)
    private LocalDate statDate;

    @Schema(description = "总办件数")
    private Integer totalCount;

    @Schema(description = "完成办件数")
    private Integer finishCount;

    @Schema(description = "超期办件数")
    private Integer timeoutCount;

    @Schema(description = "驳回办件数")
    private Integer rejectCount;

    @Schema(description = "平均办理时长（分钟）")
    private Integer avgTime;

    @Schema(description = "最长办理时长（分钟）")
    private Integer maxTime;

    @Schema(description = "最短办理时长（分钟）")
    private Integer minTime;

    @Schema(description = "平均满意度")
    private BigDecimal satisfactionAvg;

    @Schema(description = "黄牌数量")
    private Integer yellowCount;

    @Schema(description = "红牌数量")
    private Integer redCount;

    public EfficiencyStatEntity toEntity() {
        EfficiencyStatEntity entity = new EfficiencyStatEntity();
        entity.setDeptId(this.deptId);
        entity.setDeptName(this.deptName);
        entity.setStatDate(this.statDate);
        entity.setTotalCount(this.totalCount);
        entity.setFinishCount(this.finishCount);
        entity.setTimeoutCount(this.timeoutCount);
        entity.setRejectCount(this.rejectCount);
        entity.setAvgTime(this.avgTime);
        entity.setMaxTime(this.maxTime);
        entity.setMinTime(this.minTime);
        entity.setSatisfactionAvg(this.satisfactionAvg);
        entity.setYellowCount(this.yellowCount);
        entity.setRedCount(this.redCount);
        return entity;
    }
}
