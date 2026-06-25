package com.gov.complaint.dto;

import cn.hutool.core.bean.BeanUtil;
import com.gov.complaint.entity.WorkEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 投诉工单入参 DTO
 */
@Data
@Schema(description = "投诉工单请求")
public class WorkDTO {

    @Schema(description = "工单ID（修改时必填）", example = "1")
    private Long id = 1L;

    @NotBlank(message = "投诉标题不能为空")
    @Schema(description = "投诉标题", required = true, example = "小区门口路灯损坏")
    private String title = "小区门口路灯损坏";

    @NotBlank(message = "投诉内容不能为空")
    @Schema(description = "投诉内容", required = true, example = "小区门口路灯已损坏一周，夜间出行存在安全隐患，请尽快维修。")
    private String content = "小区门口路灯已损坏一周，夜间出行存在安全隐患，请尽快维修。";

    @NotNull(message = "投诉分类不能为空")
    @Schema(description = "分类ID", required = true, example = "1")
    private Long categoryId = 1L;

    @Schema(description = "投诉类型", example = "市政设施")
    private String complaintType = "市政设施";

    @Schema(description = "投诉人电话", example = "13800138000")
    private String userPhone = "13800138000";

    @Schema(description = "处理部门ID", example = "1")
    private Long deptId = 1L;

    @Schema(description = "处理部门名称", example = "市政管理局")
    private String deptName = "市政管理局";

    @Schema(description = "处理人ID", example = "1")
    private Long handlerId = 1L;

    @Schema(description = "处理人姓名", example = "张三")
    private String handlerName = "张三";

    @Schema(description = "工单状态：0待分办 1已分办 2处理中 3已回复 4已结案", example = "0")
    private String status = "0";

    @Schema(description = "满意度评分", example = "5")
    private Integer satisfaction = 5;

    @Schema(description = "备注", example = "已联系相关部门")
    private String remark = "已联系相关部门";

    /** 转为 Entity，供 Service 层使用 */
    public WorkEntity toEntity() {
        WorkEntity entity = new WorkEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
