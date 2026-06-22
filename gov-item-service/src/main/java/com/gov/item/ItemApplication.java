package com.gov.item;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 事项管理服务启动类
 * 组员A负责：事项录入、发布、变更、下架、分类查询
 */
@SpringBootApplication(scanBasePackages = {"com.gov.item", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.item.feign", "com.gov.common"})
@MapperScan("com.gov.item.mapper")
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        事项管理服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8082/doc.html");
        System.out.println("==========================================");
    }
}