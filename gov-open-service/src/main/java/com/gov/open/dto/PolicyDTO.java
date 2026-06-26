package com.gov.open.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * 政策法规DTO
 */
@Data
@Schema(description = "政策法规请求")
public class PolicyDTO {

    @NotBlank(message = "政策名称不能为空")
    @Schema(description = "政策名称", required = true)
    private String policyName;

    @Schema(description = "政策类型")
    private String policyType;

    @Schema(description = "政策内容")
    private String content;

    @Schema(description = "发布日期")
    private LocalDate publishDate;

    @Schema(description = "实施日期")
    private LocalDate implementDate;

    @Schema(description = "生效状态")
    private String effectiveStatus;

    @Schema(description = "文件URL")
    private String fileUrl;

    @Schema(description = "关键词")
    private String keywords;

    @Schema(description = "状态：0草稿 1已发布")
    private String status;
}
