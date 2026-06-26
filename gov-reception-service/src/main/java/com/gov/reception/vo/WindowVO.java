package com.gov.reception.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 窗口VO
 */
@Data
@Schema(description = "窗口信息")
public class WindowVO implements Serializable {

    @Schema(description = "窗口ID")
    private Long id;

    @Schema(description = "窗口编号")
    private String windowNo;

    @Schema(description = "窗口名称")
    private String windowName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "窗口工作人员ID")
    private Long staffId;

    @Schema(description = "状态：0关闭 1开放 2暂停")
    private String status;

    @Schema(description = "状态描述")
    private String statusDesc;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
