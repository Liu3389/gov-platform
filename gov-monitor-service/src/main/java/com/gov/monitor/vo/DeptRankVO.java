package com.gov.monitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Schema(description = "部门效率排名")
public class DeptRankVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "办件数")
    private Long totalCount;

    @Schema(description = "平均办理时长（分钟）")
    private Integer avgTime;

    @Schema(description = "满意度")
    private BigDecimal satisfactionAvg;
}
