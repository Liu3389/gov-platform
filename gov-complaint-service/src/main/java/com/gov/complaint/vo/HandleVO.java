package com.gov.complaint.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "处理反馈信息")
public class HandleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "反馈ID") private Long id;
    @Schema(description = "工单ID") private Long workId;
    @Schema(description = "处理人ID") private Long handlerId;
    @Schema(description = "处理人姓名") private String handlerName;
    @Schema(description = "处理类型") private String handleType;
    @Schema(description = "处理内容") private String handleContent;
    @Schema(description = "处理时间") private LocalDateTime handleTime;
    @Schema(description = "下一部门ID") private Long nextDeptId;
    @Schema(description = "下一部门名称") private String nextDeptName;
    @Schema(description = "创建时间") private LocalDateTime createTime;
    @Schema(description = "更新时间") private LocalDateTime updateTime;
}
