package com.gov.monitor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Schema(description = "操作日志请求")
public class LogDTO {

    @Schema(description = "操作人ID")
    private Long userId;

    @NotBlank(message = "操作人姓名不能为空")
    @Size(max = 50, message = "操作人姓名长度不能超过50")
    @Schema(description = "操作人姓名")
    private String userName;

    @Schema(description = "操作部门ID")
    private Long deptId;

    @Size(max = 100, message = "操作部门名称长度不能超过100")
    @Schema(description = "操作部门名称")
    private String deptName;

    @NotBlank(message = "操作模块不能为空")
    @Size(max = 50, message = "操作模块长度不能超过50")
    @Schema(description = "操作模块")
    private String module;

    @NotBlank(message = "操作动作不能为空")
    @Size(max = 100, message = "操作动作长度不能超过100")
    @Schema(description = "操作动作")
    private String action;

    @Size(max = 200, message = "请求方法长度不能超过200")
    @Schema(description = "请求方法全限定名")
    private String method;

    @Size(max = 500, message = "请求URL长度不能超过500")
    @Schema(description = "请求URL")
    private String requestUrl;

    @Size(max = 20, message = "请求类型长度不能超过20")
    @Schema(description = "请求类型：GET/POST/PUT/DELETE")
    private String requestType;

    @Schema(description = "请求参数")
    private String requestParams;

    @Schema(description = "响应数据")
    private String responseData;

    @Schema(description = "操作IP")
    private String operateIp;

    @Size(max = 100, message = "操作地点长度不能超过100")
    @Schema(description = "操作地点")
    private String operateLocation;

    @Schema(description = "执行时间（毫秒）")
    private Integer executeTime;

    @Schema(description = "状态：1成功 0失败")
    private Integer status;

    @Size(max = 500, message = "错误消息长度不能超过500")
    @Schema(description = "错误消息")
    private String errorMsg;
}
