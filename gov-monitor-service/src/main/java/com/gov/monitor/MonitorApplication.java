package com.gov.monitor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 监察审计服务启动类
 * 组长维护：办件统计、效能分析、数据大屏、审计日志、报表导出
 */
@SpringBootApplication(scanBasePackages = {"com.gov.monitor", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.monitor.feign", "com.gov.common"})
@MapperScan("com.gov.monitor.mapper")
public class MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        监察审计服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8090/doc.html");
        System.out.println("==========================================");
    }
}