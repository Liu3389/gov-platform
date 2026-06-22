package com.gov.reception;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 统一受理服务启动类
 * 组员B负责：一窗登记、材料接收、业务分发、统一出件、进度查询
 */
@SpringBootApplication(scanBasePackages = {"com.gov.reception", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.reception.feign", "com.gov.common"})
@MapperScan("com.gov.reception.mapper")
public class ReceptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReceptionApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        统一受理服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8083/doc.html");
        System.out.println("==========================================");
    }
}