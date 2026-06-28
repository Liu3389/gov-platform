package com.gov.common.config;

import org.springframework.context.annotation.Configuration;

/**
 * Seata 分布式事务配置（第一阶段：仅引入依赖和配置骨架）
 *
 * <p>第一阶段说明：当前仅引入 seata-spring-boot-starter 依赖并提供配置类骨架。
 * 待 Seata Server（TC）部署完成后，再启用 DataSourceProxy 代理和全局事务。
 * 完整接入步骤：
 * <ol>
 *   <li>部署 Seata Server（TC），registry.conf 指向 Nacos</li>
 *   <li>各服务 application-dev.yml 添加 seata 配置</li>
 *   <li>取消下方 DataSourceProxy Bean 注释</li>
 *   <li>在需要分布式事务的方法上添加 @GlobalTransactional</li>
 * </ol>
 */
@Configuration
public class SeataConfig {

    /**
     * Seata 数据源代理 —— 暂时禁用，等 Seata Server 部署后启用
     *
     * <p>取消注释并注入 DataSource 即可启用：</p>
     * <pre>{@code
     * @Bean
     * public DataSourceProxy dataSourceProxy(DataSource dataSource) {
     *     return new DataSourceProxy(dataSource);
     * }
     * }</pre>
     */
}
