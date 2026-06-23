---
alwaysApply: false
description: 服务YAML配置规范。当新建服务、修改application-dev.yml、配置端口/Druid/Redis/MyBatis-Plus时应用此规则。涵盖端口分配表、Nacos配置、逻辑删除三项、特殊服务额外配置。
---
# 服务 YAML 配置模板

## 通用配置模板（有数据库的服务）

```yaml
server:
  port: {分配端口}

spring:
  application:
    name: {服务名}
  config:
    import: "optional:nacos:gov-platform-common.yaml?group=DEFAULT_GROUP"
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        group: DEFAULT_GROUP
        namespace: gov-platform
        username: nacos
        password: nacos
      config:
        server-addr: localhost:8848
        group: DEFAULT_GROUP
        namespace: gov-platform
        file-extension: yaml
        username: nacos
        password: nacos
  datasource:
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3307/{数据库名}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: root123456
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      validation-query: SELECT 1
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
  redis:
    host: localhost
    port: 6379
    database: {分配编号}

mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.gov.{服务名}.entity
  global-config:
    db-config:
      id-type: assign_id
      logic-delete-field: deleted
      logic-delete-value: 1
      logic-not-delete-value: 0
  configuration:
    map-underscore-to-camel-case: true

knife4j:
  enable: true

logging:
  level:
    com.gov.{服务名}: debug
```

## gov-activiti-service 额外必须配置

```yaml
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
      - org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration

activiti:
  database-schema-update: true
  history-level: full
  async-executor-activate: true
  job-executor-activate: true
  check-process-definitions: false
```

## gov-gateway 额外必须配置

```yaml
knife4j:
  gateway:
    enabled: true
    strategy: manual
    routes:
      - name: 用户认证服务
        serviceName: gov-user-service
        url: /user/v3/api-docs
        context-path: /api/v1/user
        order: 1
      # ... 每新增一个服务，在此追加一个 route

spring:
  cloud:
    gateway:
      routes:
        # Knife4j 文档路由（RewritePath，必须在业务路由之前）
        - id: knife4j-item-docs
          uri: lb://gov-item-service
          predicates:
            - Path=/item/v3/api-docs/**
          filters:
            - RewritePath=/item/(?<remaining>.*), /$\{remaining}
        # 业务路由
        - id: gov-item-service
          uri: lb://gov-item-service
          predicates:
            - Path=/api/v1/item/**
          filters:
            - StripPrefix=2
```

## 端口分配表

| 服务 | 端口 | Redis DB |
|------|------|:--:|
| gov-gateway | 8091 | — |
| gov-user-service | 8081 | 1 |
| gov-item-service | 8092 | 2 |
| gov-reception-service | 8083 | 3 |
| gov-activiti-service | 8084 | 4 |
| gov-license-service | 8085 | 5 |
| gov-complaint-service | 8086 | 6 |
| gov-open-service | 8087 | 7 |
| gov-datashare-service | 8088 | 8 |
| gov-message-service | 8089 | 9 |
| gov-monitor-service | 8090 | 10 |

## 铁律

1. 新建服务必须用上表未占用的端口
2. logic-delete-field / logic-delete-value / logic-not-delete-value 三项缺一不可
3. nacos namespace 必须填 `gov-platform`
4. Druid 八项子配置不能少（initial-size 到 test-on-return）
5. gov-gateway 不能用 `discover` 模式
6. Sentinel Dashboard 必须用 8858 端口（不可 8080）
