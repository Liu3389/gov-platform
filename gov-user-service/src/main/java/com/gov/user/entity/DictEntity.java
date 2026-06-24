package com.gov.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据字典实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_dict_data")
@Schema(description = "数据字典")
public class DictEntity extends BaseEntity {

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "父级编码")
    private String parentCode;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;
}
