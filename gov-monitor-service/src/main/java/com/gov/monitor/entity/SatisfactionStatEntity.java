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
@TableName("t_satisfaction_stat")
@Schema(description = "满意度统计")
public class SatisfactionStatEntity extends BaseEntity {

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
