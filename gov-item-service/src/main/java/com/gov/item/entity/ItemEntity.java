package com.gov.item.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 事项信息实体（示例）
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_item_base")
@Schema(description = "事项信息")
public class ItemEntity extends BaseEntity {

    @Schema(description = "事项编码")
    private String itemCode;

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "事项分类ID")
    private Long categoryId;

    @Schema(description = "事项类型：1行政许可 2公共服务 3行政确认 4行政奖励 5行政给付")
    private Integer itemType;

    @Schema(description = "事项级别：1国家级 2省级 3市级 4区县级")
    private Integer itemLevel;

    @Schema(description = "实施类型：1即办 2承诺件 3上报件")
    private Integer implementType;

    @Schema(description = "承诺时限（工作日）")
    private Integer timeLimit;

    @Schema(description = "是否收费：0否 1是")
    private Integer feeFlag;

    @Schema(description = "收费标准")
    private String feeStandard;

    @Schema(description = "是否支持网办：0否 1是")
    private Integer onlineFlag;

    @Schema(description = "是否支持窗口：0否 1是")
    private Integer windowFlag;

    @Schema(description = "版本号")
    private Integer version;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态：0草稿 1发布 2下架")
    private Integer status;
}
