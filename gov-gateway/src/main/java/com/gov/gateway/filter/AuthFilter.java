package com.gov.gateway.filter;

import com.gov.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * JWT 鉴权全局过滤器
 * 校验 Token，放行白名单路径
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    /** 白名单路径前缀（无需鉴权） */
    private static final List<String> WHITE_LIST_PREFIX = Arrays.asList(
        "/api/v1/user/login",
        "/api/v1/user/register",
        "/api/v1/user/captcha",
        "/api/v1/open/notice",
        "/api/v1/open/policy",
        "/doc.html",
        "/webjars",
        "/v3/api-docs",
        "/user/v3",
        "/item/v3",
        "/reception/v3",
        "/workflow/v3",
        "/license/v3",
        "/complaint/v3",
        "/open/v3",
        "/share/v3",
        "/message/v3",
        "/monitor/v3",
        "/favicon.ico"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 白名单直接放行
        if (isWhiteList(path)) {
            return chain.filter(exchange);
        }

        // 获取 Token（优先标准 Authorization 头，兼容 Knife4j 的 authorize 头）
        String token = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (token == null || token.isEmpty()) {
            token = request.getHeaders().getFirst("authorize");
        }
        if (token == null || token.isEmpty()) {
            return unauthorizedResponse(exchange, "未提供认证令牌");
        }

        // 去掉 Bearer 前缀
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 校验 Token
        if (!JwtUtil.isValid(token)) {
            return unauthorizedResponse(exchange, "认证令牌无效或已过期");
        }

        // 将用户信息传递给下游服务（统一使用标准 Authorization 头）
        Long userId = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        Long deptId = JwtUtil.getDeptId(token);

        ServerHttpRequest.Builder builder = request.mutate()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .header("X-User-Id", String.valueOf(userId))
            .header("X-Username", username);

        if (deptId != null) {
            builder.header("X-Dept-Id", String.valueOf(deptId));
        }

        String roles = JwtUtil.getRoles(token);
        if (roles != null && !roles.isEmpty()) {
            builder.header("X-Roles", roles);
        }

        ServerHttpRequest finalRequest = builder.build();

        return chain.filter(exchange.mutate().request(finalRequest).build());
    }

    @Override
    public int getOrder() {
        return -100;
    }

    /**
     * 判断是否为白名单路径
     */
    private boolean isWhiteList(String path) {
        return WHITE_LIST_PREFIX.stream().anyMatch(path::startsWith);
    }

    /**
     * 返回未授权响应
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String body = String.format(
            "{\"code\":401,\"message\":\"%s\",\"data\":null,\"timestamp\":%d,\"traceId\":\"%s\"}",
            message, System.currentTimeMillis(), ""
        );

        DataBuffer buffer = response.bufferFactory()
            .wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }
}