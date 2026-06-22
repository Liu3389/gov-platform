package com.gov.license;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 电子证照服务启动类
 * 组员C负责：证照生成、电子签章、二维码核验、下载授权
 */
@SpringBootApplication(scanBasePackages = {"com.gov.license", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.license.feign", "com.gov.common"})
@MapperScan("com.gov.license.mapper")
public class LicenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenseApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        电子证照服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8085/doc.html");
        System.out.println("==========================================");
    }
}