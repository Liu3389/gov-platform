package com.gov.complaint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "督办记录信息")
public class SuperviseVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "督办ID") private Long id;
    @Schema(description = "工单ID") private Long workId;
    @Schema(description = "督办级别") private String superviseLevel;
    @Schema(description = "督办时间") private LocalDateTime superviseTime;
    @Schema(description = "督办人ID") private Long superviseBy;
    @Schema(description = "督办内容") private String superviseContent;
    @Schema(description = "办理期限") private LocalDateTime deadline;
    @Schema(description = "状态") private String status;
    @Schema(description = "创建时间") private LocalDateTime createTime;
    @Schema(description = "更新时间") private LocalDateTime updateTime;
}
