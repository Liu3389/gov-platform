package com.gov.complaint.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.complaint.entity.HandleEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 处理反馈入参 DTO
 */
@Data
@Schema(description = "处理反馈请求")
public class HandleDTO {

    @Schema(description = "反馈ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", required = true, example = "1")
    private Long workId = 1L;

    @Schema(description = "处理类型", example = "电话回访")
    private String handleType = "电话回访";

    @NotBlank(message = "处理内容不能为空")
    @Schema(description = "处理内容", required = true, example = "已安排维修人员前往处理，预计2个工作日内修复。")
    private String handleContent = "已安排维修人员前往处理，预计2个工作日内修复。";

    @Schema(description = "下一部门ID", example = "2")
    private Long nextDeptId = 2L;

    @Schema(description = "下一部门名称", example = "市政工程处")
    private String nextDeptName = "市政工程处";

    public HandleEntity toEntity() {
        HandleEntity entity = new HandleEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
