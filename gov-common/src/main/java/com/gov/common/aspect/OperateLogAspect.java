package com.gov.common.aspect;

import com.alibaba.fastjson2.JSON;
import com.gov.common.annotation.Log;
import com.gov.common.event.OperateLogEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 操作日志 AOP 切面 —— 拦截 @Log 注解的方法，自动记录操作日志
 *
 * <p>工作流程：
 * <ol>
 *   <li>拦截带有 @Log 注解的 Controller 方法</li>
 *   <li>记录请求信息（用户、IP、URL、参数）</li>
 *   <li>执行原方法，记录耗时和结果</li>
 *   <li>异步发布 OperateLogEvent，由监听器落库</li>
 * </ol>
 *
 * <p>使用示例：
 * <pre>
 * &#064;Log(module = "用户管理", action = "新增用户")
 * &#064;PostMapping
 * public Result&lt;Void&gt; add(&#064;RequestBody UserEntity user) { ... }
 * </pre>
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperateLogAspect {

    private final ApplicationEventPublisher eventPublisher;

    /**
     * 环绕通知：拦截 @Log 注解方法
     */
    @Around("@annotation(logAnnotation)")
    public Object around(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 构建日志事件基础信息
        OperateLogEvent.OperateLogEventBuilder eventBuilder = OperateLogEvent.builder()
                .operateTime(LocalDateTime.now())
                .module(logAnnotation.module())
                .action(logAnnotation.action())
                .method(joinPoint.getSignature().toShortString())
                .status(1); // 默认成功

        if (request != null) {
            eventBuilder
                    .requestUrl(request.getRequestURI())
                    .requestType(request.getMethod())
                    .operateIp(getClientIp(request))
                    .userId(parseLong(request.getHeader("X-User-Id")))
                    .userName(request.getHeader("X-Username"))
                    .deptId(parseLong(request.getHeader("X-Dept-Id")))
                    .deptName(request.getHeader("X-Dept-Name"));
        }

        // 记录请求参数（可选）
        if (logAnnotation.recordParams()) {
            eventBuilder.requestParams(getRequestParams(joinPoint));
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            eventBuilder.status(0);
            eventBuilder.errorMsg(e.getMessage());
            throw e;
        } finally {
            long executeTime = System.currentTimeMillis() - startTime;
            eventBuilder.executeTime(executeTime);

            // 记录响应数据（可选）
            if (logAnnotation.recordResult() && result != null) {
                try {
                    eventBuilder.responseData(JSON.toJSONString(result));
                } catch (Exception e) {
                    log.debug("序列化响应数据失败", e);
                }
            }

            // 异步发布事件（不阻塞主流程）
            try {
                eventPublisher.publishEvent(eventBuilder.build());
            } catch (Exception e) {
                log.error("发布操作日志事件失败", e);
            }
        }
    }

    /**
     * 获取请求参数 JSON 字符串（安全截断，防止参数过长）
     */
    private String getRequestParams(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            if (paramNames == null || args == null || args.length == 0) {
                return "";
            }

            // 过滤掉 HttpServletRequest、HttpServletResponse 等无法序列化的参数
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof javax.servlet.http.HttpServletRequest
                        || arg instanceof javax.servlet.http.HttpServletResponse
                        || arg instanceof org.springframework.web.multipart.MultipartFile) {
                    continue;
                }
                if (!first) sb.append(", ");
                sb.append(paramNames[i]).append(": ");
                try {
                    String json = JSON.toJSONString(arg);
                    // 截断过长参数
                    if (json.length() > 2000) {
                        json = json.substring(0, 2000) + "...";
                    }
                    sb.append(json);
                } catch (Exception e) {
                    sb.append("\"[序列化失败]\"");
                }
                first = false;
            }
            sb.append("}");
            return sb.toString();
        } catch (Exception e) {
            log.debug("获取请求参数失败", e);
            return "";
        }
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多级代理取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private Long parseLong(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
