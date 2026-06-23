package com.gov.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 操作日志事件 —— 由 @Log AOP 切面发布，异步持久化到数据库
 *
 * <p>通过 Spring ApplicationEvent 机制传递，监听方负责落库。</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperateLogEvent {

    /** 操作人ID */
    private Long userId;

    /** 操作人姓名 */
    private String userName;

    /** 操作部门ID */
    private Long deptId;

    /** 操作部门名称 */
    private String deptName;

    /** 操作模块 */
    private String module;

    /** 操作动作 */
    private String action;

    /** 请求方法全限定名 */
    private String method;

    /** 请求URL */
    private String requestUrl;

    /** 请求类型：GET/POST/PUT/DELETE */
    private String requestType;

    /** 请求参数（JSON 字符串） */
    private String requestParams;

    /** 响应数据（JSON 字符串，仅 recordResult=true 时记录） */
    private String responseData;

    /** 操作时间 */
    private LocalDateTime operateTime;

    /** 操作IP */
    private String operateIp;

    /** 执行耗时（毫秒） */
    private Long executeTime;

    /** 状态：1成功 0失败 */
    private Integer status;

    /** 错误消息（失败时） */
    private String errorMsg;
}
