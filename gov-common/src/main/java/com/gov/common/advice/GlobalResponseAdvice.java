package com.gov.common.advice;

import com.gov.common.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Set;

/**
 * 全局响应拦截器 —— 强制所有 Controller 返回值自动包装为 Result&lt;T&gt;
 *
 * <p>安全网机制：如果某个 AI 开发者忘记用 Result.success() 包装返回值，
 * 此拦截器会自动包装，保证前端收到的始终是统一格式。</p>
 *
 * <pre>
 * // 以下两种写法效果相同：
 * return Result.success(data);        // 手动包装 → 拦截器跳过
 * return data;                        // 裸返回 → 拦截器自动包装为 Result.success(data)
 * </pre>
 *
 * <p>跳过包装的情况：
 * <ul>
 *   <li>返回值已经是 Result（避免二次包装）</li>
 *   <li>返回值是 ResponseEntity</li>
 *   <li>返回类型是 void</li>
 *   <li>请求路径是 Knife4j / SpringDoc 文档资源</li>
 *   <li>请求路径是 /actuator 健康检查</li>
 * </ul>
 */
@RestControllerAdvice(basePackages = "com.gov")
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    /** 不参与包装的路径前缀 */
    private static final Set<String> EXCLUDE_PATHS = Set.of(
            "/v3/api-docs",
            "/doc.html",
            "/webjars",
            "/swagger-resources",
            "/swagger-ui",
            "/actuator"
    );

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 返回值已经是 Result，跳过
        if (returnType.getParameterType().equals(Result.class)) {
            return false;
        }
        // 返回值是 ResponseEntity，跳过
        if (returnType.getParameterType().equals(ResponseEntity.class)) {
            return false;
        }
        // 返回类型是 void，跳过
        if (returnType.getParameterType().equals(void.class) || returnType.getParameterType().equals(Void.class)) {
            return false;
        }
        // String 类型跳过（避免类型转换异常）
        if (returnType.getParameterType().equals(String.class)) {
            return false;
        }
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 从 request 中获取实际请求路径
        String path = getRequestPath(request);
        if (path != null && isExcludedPath(path)) {
            return body;
        }

        // 运行时已经是 Result（兜底判断）
        if (body instanceof Result) {
            return body;
        }

        // 自动包装
        return Result.success(body);
    }

    private String getRequestPath(ServerHttpRequest request) {
        if (request instanceof ServletServerHttpRequest) {
            return ((ServletServerHttpRequest) request).getServletRequest().getRequestURI();
        }
        return request.getURI().getPath();
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDE_PATHS.stream().anyMatch(path::startsWith);
    }
}
