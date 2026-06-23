package com.gov.common.annotation;

import java.lang.annotation.*;

/**
 * 权限校验注解
 * 标记在 Controller 方法上，校验当前用户是否拥有指定权限
 *
 * <pre>
 * &#064;RequirePermission("user:add")
 * public Result<Void> add(@RequestBody UserEntity user) { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /** 权限编码，如 user:add、item:delete */
    String value();

    /** 校验模式：AND=需全部满足, OR=满足任一即可（默认OR） */
    Mode mode() default Mode.OR;

    enum Mode {
        AND, OR
    }
}
