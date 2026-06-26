package com.gov.reception.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 办件提交DTO
 */
@Data
@Schema(description = "办件提交请求")
public class RecordSubmitDTO {

    @NotNull(message = "事项ID不能为空")
    @Schema(description = "事项ID", required = true)
    private Long itemId;

    @NotNull(message = "受理部门ID不能为空")
    @Schema(description = "受理部门ID", required = true)
    private Long deptId;

    @Schema(description = "表单数据（JSON格式）")
    private String formData;

    @Schema(description = "申报材料列表")
    private List<MaterialDTO> materials;

    @Schema(description = "备注")
    private String remark;
}
