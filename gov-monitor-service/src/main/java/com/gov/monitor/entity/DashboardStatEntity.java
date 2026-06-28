package com.gov.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_dashboard_stat")
@Schema(description = "数据大屏统计")
public class DashboardStatEntity extends BaseEntity {

    /** 表中缺少此列，排除映射 */
    @TableField(exist = false)
    private LocalDateTime updateTime;

    /** 表中缺少此列，排除映射 */
    @TableField(exist = false)
    private Long createBy;

    /** 表中缺少此列，排除映射 */
    @TableField(exist = false)
    private Long updateBy;

    @Schema(description = "统计类型：total/finish/timeout/satisfaction")
    private String statType;

    @Schema(description = "统计值")
    private Long statValue;

    @Schema(description = "统计日期")
    private LocalDate statDate;

    @Schema(description = "统计小时")
    private Integer statHour;

    @Schema(description = "部门ID")
    private Long deptId;
}
