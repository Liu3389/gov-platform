package com.gov.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j 接口文档配置
 */
@Configuration
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("智慧政务一体化便民服务平台 API")
                        .version("1.0.0")
                        .description("智慧政务一体化便民服务平台接口文档，实现\"一网通办、一窗受理、协同办理\"")
                        .contact(new Contact()
                                .name("政务服务平台开发团队")
                                .email("gov@example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}