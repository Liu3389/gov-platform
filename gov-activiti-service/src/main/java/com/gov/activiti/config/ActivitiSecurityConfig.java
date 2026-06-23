package com.gov.activiti.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Activiti Security 配置 —— 放行所有请求
 *
 * <p>网关已通过 JWT 统一鉴权，Activiti 服务不需要再做认证。
 * 此处仅为了满足 Activiti 对 Spring Security 的依赖，实际不拦截任何请求。</p>
 */
@Configuration
@EnableWebSecurity
public class ActivitiSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .anyRequest().permitAll()
            .and()
            .formLogin().disable()
            .httpBasic().disable();
        return http.build();
    }
}
