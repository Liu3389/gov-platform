package com.gov.common.aspect;

import com.gov.common.annotation.RequirePermission;
import com.gov.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * 权限校验 AOP 切面 —— 拦截 @RequirePermission 注解，校验当前用户是否有权限
 *
 * <p>权限规则：
 * <ul>
 *   <li>ROLE_ADMIN = 超级管理员，拥有所有权限</li>
 *   <li>其他用户必须有对应角色才能执行操作</li>
 *   <li>无 Token（无 X-Roles 头） → 401</li>
 *   <li>Token 有效但无对应角色 → 403</li>
 * </ul>
 *
 * <p>依赖：AuthFilter 已将 JWT 中的 roles 写入 X-Roles 请求头</p>
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Around("@annotation(permission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission permission) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // 非 HTTP 请求（如定时任务），放行
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String rolesHeader = request.getHeader("X-Roles");

        // 无角色信息 → Token 未携带角色 → 权限不足
        if (rolesHeader == null || rolesHeader.isEmpty()) {
            log.warn("[权限拦截] 请求缺少角色信息: URI={}, 需要权限={}", request.getRequestURI(), permission.value());
            throw new BusinessException(403, "权限不足：当前用户无角色信息");
        }

        List<String> userRoles = Arrays.asList(rolesHeader.split(","));

        // ROLE_ADMIN 拥有所有权限
        if (userRoles.contains("ROLE_ADMIN")) {
            return joinPoint.proceed();
        }

        // 检查是否有所需权限
        String required = permission.value();
        boolean hasPermission = false;
        for (String role : userRoles) {
            // 角色编码匹配（如 ROLE_1 对应 roleId=1，未来可扩展为权限码映射）
            if (role.equals(required) || role.contains(required)) {
                hasPermission = true;
                break;
            }
        }

        if (!hasPermission) {
            log.warn("[权限拦截] 用户无权限: URI={}, 需要={}, 用户拥有={}",
                    request.getRequestURI(), permission.value(), rolesHeader);
            throw new BusinessException(403, "权限不足：需要权限 " + permission.value());
        }

        return joinPoint.proceed();
    }
}
