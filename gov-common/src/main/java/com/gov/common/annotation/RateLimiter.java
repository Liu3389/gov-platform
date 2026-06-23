package com.gov.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 接口限流注解
 * 标记在 Controller 方法上，限制接口调用频率
 *
 * <pre>
 * &#064;RateLimiter(key = "login", permits = 5, timeout = 60, timeUnit = TimeUnit.SECONDS)
 * public Result<LoginVO> login(@RequestBody LoginDTO dto) { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimiter {

    /** 限流 key（默认使用方法全路径名） */
    String key() default "";

    /** 允许的请求次数 */
    int permits() default 10;

    /** 时间窗口 */
    long timeout() default 1;

    /** 时间单位 */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
