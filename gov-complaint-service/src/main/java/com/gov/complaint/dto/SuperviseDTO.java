package com.gov.complaint.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.complaint.entity.SuperviseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 督办记录入参 DTO
 */
@Data
@Schema(description = "督办记录请求")
public class SuperviseDTO {

    @Schema(description = "督办ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotNull(message = "工单ID不能为空")
    @Schema(description = "工单ID", required = true, example = "1")
    private Long workId = 1L;

    @NotBlank(message = "督办级别不能为空")
    @Schema(description = "督办级别", required = true, example = "一级")
    private String superviseLevel = "一级";

    @Schema(description = "督办内容", example = "该工单已超时未处理，请尽快跟进。")
    private String superviseContent = "该工单已超时未处理，请尽快跟进。";

    @Schema(description = "办理期限", example = "2026-07-01T18:00:00")
    private LocalDateTime deadline = LocalDateTime.of(2026, 7, 1, 18, 0, 0);

    @Schema(description = "状态", example = "0")
    private String status = "0";

    public SuperviseEntity toEntity() {
        SuperviseEntity entity = new SuperviseEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
