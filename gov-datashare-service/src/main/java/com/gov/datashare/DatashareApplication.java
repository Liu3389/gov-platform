package com.gov.datashare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 数据共享服务启动类
 * 组员D负责：数据源注册、接口发布订阅、共享目录、调用统计
 */
@SpringBootApplication(scanBasePackages = {"com.gov.datashare", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.datashare.feign", "com.gov.common"})
@MapperScan("com.gov.datashare.mapper")
public class DatashareApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatashareApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        数据共享服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8088/doc.html");
        System.out.println("==========================================");
    }
}