package com.gov.common.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Feign 请求拦截器 —— 自动透传用户上下文到下游服务
 *
 * <p>服务间 Feign 调用时，自动从当前请求中提取以下 Header 并添加到 Feign 请求中：
 * <ul>
 *   <li>Authorization — JWT Token</li>
 *   <li>X-User-Id — 当前用户ID</li>
 *   <li>X-Username — 当前用户名</li>
 *   <li>X-Dept-Id — 当前部门ID</li>
 * </ul>
 *
 * <p>这样下游服务的 Controller 也能通过 request.getHeader() 获取当前用户信息，
 * 无需每个 Feign 方法的参数中显式传递 userId。</p>
 *
 * <p>Spring Cloud OpenFeign 会自动发现所有 RequestInterceptor Bean 并注册。</p>
 */
@Slf4j
@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();

        // 透传 JWT Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isEmpty()) {
            template.header("Authorization", authHeader);
        }

        // 透传用户ID（网关 JWT 鉴权后设置）
        String userId = request.getHeader("X-User-Id");
        if (userId != null && !userId.isEmpty()) {
            template.header("X-User-Id", userId);
        }

        // 透传用户名
        String username = request.getHeader("X-Username");
        if (username != null && !username.isEmpty()) {
            template.header("X-Username", username);
        }

        // 透传部门ID
        String deptId = request.getHeader("X-Dept-Id");
        if (deptId != null && !deptId.isEmpty()) {
            template.header("X-Dept-Id", deptId);
        }
    }
}
