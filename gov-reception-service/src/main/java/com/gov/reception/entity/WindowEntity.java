package com.gov.reception.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 窗口信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_reception_window")
@Schema(description = "窗口信息")
public class WindowEntity extends BaseEntity {

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
}
