package com.gov.common.config;

import com.gov.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * JWT 密钥配置 —— 从 Nacos / application.yml 中注入 JWT 密钥
 *
 * <p>支持密钥轮换：修改 Nacos 配置中的 jwt.secret 后重启服务即可生效。
 * 若不配置，则使用 JwtUtil 内置的默认密钥。</p>
 */
@Slf4j
@Configuration
public class JwtConfig {

    @Value("${jwt.secret:GovPlatformSecretKey2024ForJwtTokenGenerationAndValidation}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        JwtUtil.setSecret(jwtSecret);
        log.info("JWT 密钥已从配置加载（来源：${jwt.secret}）");
    }
}
