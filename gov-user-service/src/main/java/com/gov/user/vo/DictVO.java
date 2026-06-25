package com.gov.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "数据字典响应")
public class DictVO implements Serializable {

    @Schema(description = "字典ID")
    private Long id;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典值")
    private String dictValue;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
