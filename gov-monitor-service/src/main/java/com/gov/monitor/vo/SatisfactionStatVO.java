package com.gov.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "满意度统计")
public class SatisfactionStatVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "统计ID")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "统计日期")
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
}
