package com.gov.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 消息通知服务启动类
 * 成员D负责：消息模板、多渠道推送、站内信、APP推送
 */
@SpringBootApplication(scanBasePackages = {"com.gov.message", "com.gov.common"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.gov.message.feign", "com.gov.common"})
@MapperScan("com.gov.message.mapper")
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class, args);
        System.out.println("==========================================");
        System.out.println("        消息通知服务启动成功！");
        System.out.println("  Knife4j 文档地址：http://localhost:8089/doc.html");
        System.out.println("==========================================");
    }
}