package com.gov.item.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.item.entity.ItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 事项新增/修改请求 DTO
 */
@Data
@Schema(description = "事项请求")
public class ItemDTO {

    @Schema(description = "事项ID（修改时必填）")
    private Long id;

    @NotBlank(message = "事项编码不能为空")
    @Schema(description = "事项编码", required = true)
    private String itemCode;

    @NotBlank(message = "事项名称不能为空")
    @Schema(description = "事项名称", required = true)
    private String itemName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "事项类型：1行政许可 2公共服务 3行政确认")
    private Integer itemType;

    @Schema(description = "状态：0草稿 1发布 2下架")
    private Integer status;

    public ItemEntity toEntity() {
        return BeanUtil.copyProperties(this, ItemEntity.class);
    }
}
