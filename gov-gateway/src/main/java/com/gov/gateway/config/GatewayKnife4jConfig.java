package com.gov.gateway.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 网关 Knife4j 聚合文档配置
 *
 * <p>网关聚合模式下，各服务的 SecurityScheme 不会自动合并。
 * 必须在网关模块单独定义 OpenAPI，才能让 Authorize 🔒 按钮生效。</p>
 *
 * <p>使用方法：
 * <ol>
 *   <li>打开 http://localhost:8091/doc.html</li>
 *   <li>点击右上角 🔒 Authorize 按钮</li>
 *   <li>输入纯 JWT Token（不含 Bearer 前缀）</li>
 *   <li>点 Authorize → Close</li>
 *   <li>之后所有请求自动携带 Authorization: Bearer &lt;token&gt;</li>
 * </ol>
 */
@Configuration
public class GatewayKnife4jConfig {

    @Bean
    @Primary
    public OpenAPI gatewayOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧政务一体化便民服务平台")
                        .version("1.0.0")
                        .description("网关聚合文档 — 包含全部已注册服务的接口"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("输入纯 JWT Token（如 eyJhbG...），点击 Authorize 后自动添加到所有请求")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }
}
