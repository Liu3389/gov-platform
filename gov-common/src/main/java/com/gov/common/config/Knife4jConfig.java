package com.gov.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Knife4j 接口文档配置
 *
 * <p>接口契约驱动开发：所有 AI 开发者通过以下入口查看其他服务的 API：
 * <ul>
 *   <li><b>网关聚合文档（推荐）</b>：<a href="http://localhost:8091/doc.html">http://localhost:8091/doc.html</a></li>
 *   <li>单服务文档：http://localhost:{服务端口}/doc.html</li>
 *   <li>OpenAPI 3.0 JSON：http://localhost:{服务端口}/v3/api-docs</li>
 * </ul>
 *
 * <p><b>契约开发流程（4个AI并行时）：</b>
 * <ol>
 *   <li>每个 AI 开发前先到网关文档查看相关服务的已有接口</li>
 *   <li>新增接口必须写 @Tag、@Operation、@Parameter/@Schema</li>
 *   <li>修改接口前必须先告知相关方，避免联调冲突</li>
 *   <li>每次提交前在网关文档中验证新接口是否正确出现</li>
 * </ol>
 */
@Configuration
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧政务一体化便民服务平台 API")
                        .version("1.0.0")
                        .description("""
                                智慧政务一体化便民服务平台接口文档
                                
                                ## 访问方式
                                - 网关聚合文档（推荐查看全部服务）：http://localhost:8091/doc.html
                                - 单服务直连：http://localhost:{服务端口}/doc.html
                                
                                ## 统一响应格式
                                ```json
                                { "code": 200, "message": "操作成功", "data": {}, "timestamp": 1718888888, "traceId": "xxx" }
                                ```
                                """)
                        .contact(new Contact()
                                .name("政务服务平台开发团队")
                                .email("gov@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8091").description("网关（推荐）"),
                        new Server().url("http://localhost:8081").description("用户服务直连"),
                        new Server().url("http://localhost:8083").description("受理服务直连")
                ))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }
}
