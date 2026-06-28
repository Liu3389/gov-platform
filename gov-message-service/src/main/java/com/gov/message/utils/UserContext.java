package com.gov.message.utils;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 用户上下文工具类
 * 从网关传递的Header中获取当前登录用户信息
 */
public class UserContext {

    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USERNAME = "X-Username";
    private static final String HEADER_DEPT_ID = "X-Dept-Id";
    private static final String HEADER_ROLES = "X-Roles";

    /**
     * 获取当前用户ID
     */
    public static Long getUserId() {
        String userId = getHeader(HEADER_USER_ID);
        return userId != null ? Long.parseLong(userId) : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getUsername() {
        return getHeader(HEADER_USERNAME);
    }

    /**
     * 获取当前用户部门ID
     */
    public static Long getDeptId() {
        String deptId = getHeader(HEADER_DEPT_ID);
        return deptId != null ? Long.parseLong(deptId) : null;
    }

    /**
     * 获取当前用户角色
     */
    public static String getRoles() {
        return getHeader(HEADER_ROLES);
    }

    /**
     * 判断当前用户是否有指定角色
     */
    public static boolean hasRole(String role) {
        String roles = getRoles();
        return roles != null && roles.contains(role);
    }

    /**
     * 判断当前用户是否是管理员
     */
    public static boolean isAdmin() {
        return hasRole("admin");
    }

    private static String getHeader(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        HttpServletRequest request = attributes.getRequest();
        return request.getHeader(name);
    }
}
