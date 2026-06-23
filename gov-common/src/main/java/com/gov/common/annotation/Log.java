package com.gov.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 标记在 Controller 方法上，自动记录操作日志到 t_operate_log 表
 *
 * <pre>
 * &#064;Log(module = "用户管理", action = "新增用户")
 * public Result<Void> add(@RequestBody UserEntity user) { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    /** 操作模块 */
    String module() default "";

    /** 操作动作 */
    String action() default "";

    /** 是否记录请求参数（默认 true） */
    boolean recordParams() default true;

    /** 是否记录响应数据（默认 false，大数据量可能影响性能） */
    boolean recordResult() default false;
}
