---
alwaysApply: false
description: 网关鉴权/路由/白名单规则。当修改AuthFilter白名单、添加网关路由、配置Knife4j聚合、修改鉴权逻辑时应用此规则。
---
# 网关鉴权 / 路由 / 白名单规则

## AuthFilter 白名单管理

文件：`gov-gateway/src/main/java/com/gov/gateway/filter/AuthFilter.java`

```java
private static final List<String> WHITE_LIST_PREFIX = Arrays.asList(
    // ===== 认证相关（无Token可访问） =====
    "/api/v1/user/login",      // 用户登录
    "/api/v1/user/register",   // 用户注册
    "/api/v1/user/captcha",    // 验证码

    // ===== 政务公开（对外无需登录） =====
    "/api/v1/open/notice",     // 通知公告
    "/api/v1/open/policy",     // 政策文件

    // ===== Knife4j 文档（不可删） =====
    "/doc.html",
    "/webjars",
    "/v3/api-docs",
    "/favicon.ico",

    // ===== Knife4j 各服务文档聚合路由 =====
    "/user/v3", "/item/v3", "/reception/v3",
    "/workflow/v3", "/license/v3", "/complaint/v3",
    "/open/v3", "/share/v3", "/message/v3", "/monitor/v3"
);
```

**添加白名单规则：**
1. 公共接口（无需登录）→ 加在"认证相关"或"政务公开"下
2. Knife4j 文档路由 → 每新增一个服务，追加一行 `/xxx/v3`
3. 添加后必须重启网关，并在 Knife4j 上验证无需 Token 可访问

## 网关路由配置

文件：`gov-gateway/src/main/resources/application-dev.yml`

每新增一个服务，必须添加两类路由：

```yaml
# 1. Knife4j 文档路由（RewritePath，必须在业务路由之前）
- id: knife4j-xxx-docs
  uri: lb://gov-xxx-service
  predicates:
    - Path=/xxx/v3/api-docs/**
  filters:
    - RewritePath=/xxx/(?<remaining>.*), /$\{remaining}

# 2. 业务路由（StripPrefix=2）
- id: gov-xxx-service
  uri: lb://gov-xxx-service
  predicates:
    - Path=/api/v1/xxx/**
  filters:
    - StripPrefix=2
```

## Knife4j 聚合配置同步

文件：`gov-gateway/src/main/resources/application-dev.yml`

每新增服务，在 `knife4j.gateway.routes` 下追加：

```yaml
- name: {中文名称}
  serviceName: {服务名}
  url: /{前缀}/v3/api-docs
  context-path: /api/v1/{前缀}
  order: {序号}
```

## 路由规则入口匹配关系

| 业务含义 | 网关路径 | 转发到服务 | 路径剥离 |
|----------|---------|-----------|---------|
| Knife4j 文档 | `/item/v3/api-docs` | `gov-item-service:/v3/api-docs` | RewritePath 剥 `/item/` |
| 业务 API | `/api/v1/item/list` | `gov-item-service:/item/list` | StripPrefix=2 剥 `/api/v1/` |

## 铁律

1. Knife4j 文档路由必须在所有业务路由**之前**
2. 白名单用 `startsWith` 前缀匹配，加路径时注意不要误放行
3. 修改 AuthFilter 后必须重启网关
4. 业务路由的 `StripPrefix` 固定为 2（剥 `/api/v1/`）
5. 网关端口 8091，不要改
6. Knife4j 必须 `manual` 模式，禁止 `discover`

## 前端跨域（CORS）配置

前端（gov-frontend，端口 5173）需要跨域访问网关 API。在网关配置文件中添加：

```yaml
spring:
  cloud:
    gateway:
      globalcors:
        cors-configurations:
          '[/**]':
            allowedOriginPatterns: "http://localhost:5173"
            allowedMethods: "*"
            allowedHeaders: "*"
            allowCredentials: true
```

> `allowCredentials: true` 时不能用 `allowedOrigins: "*"`，必须用 `allowedOriginPatterns`。
