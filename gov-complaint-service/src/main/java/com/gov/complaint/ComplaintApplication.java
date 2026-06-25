package com.gov.complaint;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 投诉建议服务启动类
 * 成员D负责：投诉提交、建议征集、分办处理、满意度评价
 */
@SpringBootApplication(scanBasePackages = {"com.gov.complaint", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.complaint.feign", "com.gov.common"})
@MapperScan("com.gov.complaint.mapper")
public class ComplaintApplication {

    public static void main(String[] args) {
        SpringApplication.run(ComplaintApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        投诉建议服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8086/doc.html");
        System.out.println("==========================================");
    }
}