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

    @Schema(description = "事项类型：1行政许可 2公共服务 3行政确认")
    private Integer itemType;

    @Schema(description = "状态：0草稿 1发布 2下架")
    private Integer status;
}
