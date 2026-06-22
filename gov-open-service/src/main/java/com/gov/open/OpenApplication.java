package com.gov.open;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 政务公开服务启动类
 * 组员D负责：信息公开目录、通知公告、政策法规、依申请公开
 */
@SpringBootApplication(scanBasePackages = {"com.gov.open", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.open.feign", "com.gov.common"})
@MapperScan("com.gov.open.mapper")
public class OpenApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        政务公开服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8087/doc.html");
        System.out.println("==========================================");
    }
}