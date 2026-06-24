package com.gov.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "数据字典请求")
public class DictDTO {

    @Schema(description = "字典ID（修改时必填）")
    private Long id;

    @NotBlank(message = "字典类型不能为空")
    @Schema(description = "字典类型")
    private String dictType;

    @NotBlank(message = "字典编码不能为空")
    @Schema(description = "字典编码")
    private String dictCode;

    @NotBlank(message = "字典名称不能为空")
    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典值")
    private String dictValue;

    @NotNull(message = "排序号不能为空")
    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态：1正常 0禁用")
    private Integer status;
}
