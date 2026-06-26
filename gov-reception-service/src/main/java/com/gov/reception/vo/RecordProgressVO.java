package com.gov.reception.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 办件进度VO
 */
@Data
@Schema(description = "办件进度")
public class RecordProgressVO implements Serializable {

    @Schema(description = "办件ID")
    private Long id;

    @Schema(description = "办件号")
    private String applyNo;

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "当前状态：0待受理 1受理中 2审批中 3办结 4驳回")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "提交时间")
    private LocalDateTime createTime;

    @Schema(description = "受理时间")
    private LocalDateTime acceptTime;

    @Schema(description = "办结时间")
    private LocalDateTime finishTime;

    @Schema(description = "进度日志列表")
    private List<ProgressLogVO> logs;

    /**
     * 进度日志
     */
    @Data
    @Schema(description = "进度日志")
    public static class ProgressLogVO implements Serializable {

        @Schema(description = "操作类型")
        private Integer actionType;

        @Schema(description = "操作描述")
        private String actionDesc;

        @Schema(description = "操作人姓名")
        private String operatorName;

        @Schema(description = "操作时间")
        private LocalDateTime operateTime;

        @Schema(description = "备注")
        private String remark;
    }
}
