package com.gov.monitor.dto;

import com.gov.monitor.entity.SatisfactionStatEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "满意度统计数据录入请求")
public class SatisfactionStatDTO {

    @NotNull(message = "部门ID不能为空")
    @Schema(description = "部门ID", required = true)
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @NotNull(message = "统计日期不能为空")
    @Schema(description = "统计日期", required = true)
    private LocalDate statDate;

    @Schema(description = "非常满意数量")
    private Integer verySatisfied;

    @Schema(description = "满意数量")
    private Integer satisfied;

    @Schema(description = "一般数量")
    private Integer neutral;

    @Schema(description = "不满意数量")
    private Integer unsatisfied;

    @Schema(description = "总评价数")
    private Integer totalCount;

    @Schema(description = "满意率（%）")
    private BigDecimal satisfactionRate;

    public SatisfactionStatEntity toEntity() {
        SatisfactionStatEntity entity = new SatisfactionStatEntity();
        entity.setDeptId(this.deptId);
        entity.setDeptName(this.deptName);
        entity.setStatDate(this.statDate);
        entity.setVerySatisfied(this.verySatisfied);
        entity.setSatisfied(this.satisfied);
        entity.setNeutral(this.neutral);
        entity.setUnsatisfied(this.unsatisfied);
        entity.setTotalCount(this.totalCount);
        entity.setSatisfactionRate(this.satisfactionRate);
        return entity;
    }
}
