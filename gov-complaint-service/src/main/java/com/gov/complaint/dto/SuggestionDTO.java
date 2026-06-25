package com.gov.complaint.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.complaint.entity.SuggestionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 建议征集入参 DTO
 */
@Data
@Schema(description = "建议征集请求")
public class SuggestionDTO {

    @Schema(description = "建议ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotBlank(message = "建议标题不能为空")
    @Schema(description = "建议标题", required = true, example = "建议增加社区公交站点")
    private String title = "建议增加社区公交站点";

    @NotBlank(message = "建议内容不能为空")
    @Schema(description = "建议内容", required = true, example = "建议在阳光社区附近增设公交站点，方便居民出行。")
    private String content = "建议在阳光社区附近增设公交站点，方便居民出行。";

    @Schema(description = "建议类型", example = "交通出行")
    private String suggestionType = "交通出行";

    @Schema(description = "状态：0待处理 1已回复 2已采纳", example = "0")
    private String status = "0";

    @Schema(description = "回复内容", example = "感谢您的建议，已纳入下季度规划。")
    private String replyContent = "感谢您的建议，已纳入下季度规划。";

    public SuggestionEntity toEntity() {
        SuggestionEntity entity = new SuggestionEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
