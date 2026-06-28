package com.gov.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_efficiency_stat")
@Schema(description = "效能统计")
public class EfficiencyStatEntity extends BaseEntity {

    /** 表中缺少此列，排除映射 */
    @TableField(exist = false)
    private LocalDateTime updateTime;

    /** 表中缺少此列，排除映射 */
    @TableField(exist = false)
    private Long createBy;

    /** 表中缺少此列，排除映射 */
    @TableField(exist = false)
    private Long updateBy;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "统计日期")
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
}
