package com.gov.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户认证服务启动类
 * 组员A负责：登录、注册、实名认证、SSO、权限校验
 */
@SpringBootApplication(scanBasePackages = {"com.gov.user", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.user.feign", "com.gov.common"})
@MapperScan("com.gov.user.mapper")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        用户认证服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8081/doc.html");
        System.out.println("==========================================");
    }
}