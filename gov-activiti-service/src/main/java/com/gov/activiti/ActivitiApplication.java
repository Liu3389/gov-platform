package com.gov.activiti;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 审批流转服务启动类【核心】
 * 组员B负责：Activiti工作流引擎、待办任务、审批流转、会签转办
 */
@SpringBootApplication(scanBasePackages = {"com.gov.activiti", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.activiti.feign", "com.gov.common"})
@MapperScan("com.gov.activiti.mapper")
public class ActivitiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        审批流转服务启动成功！【核心】");
        System.out.println("  Knife4j 文档地址：http://localhost:8084/doc.html");
        System.out.println("==========================================");
    }
}