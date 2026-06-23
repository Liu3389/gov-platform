package com.gov.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.gov.common.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 操作日志实体 —— 与 t_operate_log 表字段一一对应
 *
 * <p>操作日志由 gov-common 的 OperateLogAspect 自动生成并异步落库，
 * 开发者只需在 Controller 方法上添加 @Log 注解即可。</p>
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_operate_log")
@Schema(description = "操作日志")
public class LogEntity extends BaseEntity {

    @Schema(description = "操作人ID")
    private Long userId;

    @Schema(description = "操作人姓名")
    private String userName;

    @Schema(description = "操作部门ID")
    private Long deptId;

    @Schema(description = "操作部门名称")
    private String deptName;

    @Schema(description = "操作模块")
    private String module;

    @Schema(description = "操作动作")
    private String action;

    @Schema(description = "请求方法全限定名")
    private String method;

    @Schema(description = "请求URL")
    private String requestUrl;

    @Schema(description = "请求类型：GET/POST/PUT/DELETE")
    private String requestType;

    @Schema(description = "请求参数")
    private String requestParams;

    @Schema(description = "响应数据")
    private String responseData;

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;

    @Schema(description = "操作IP")
    private String operateIp;

    @Schema(description = "操作地点")
    private String operateLocation;

    @Schema(description = "执行时间（毫秒）")
    private Integer executeTime;

    @Schema(description = "状态：1成功 0失败")
    private Integer status;

    @Schema(description = "错误消息")
    private String errorMsg;
}
