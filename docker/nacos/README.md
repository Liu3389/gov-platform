# Nacos 配置说明

本目录包含 Nacos 配置中心的初始化配置和自动初始化脚本。

## 自动初始化（推荐）

`docker-compose up -d` 时会自动启动 `nacos-init` 容器，完成以下操作：

1. 等待 Nacos 完全就绪
2. 创建命名空间 `gov-platform`（智慧政务平台）
3. 导入共享配置 `gov-platform-common.yaml`
4. 导入 Sentinel 流控规则 `gov-sentinel-flow-rules.json`

**无需手动操作**，初始化完成后即可启动微服务。

验证方法：访问 http://localhost:8848/nacos，登录 `nacos/nacos`，在 `gov-platform` 命名空间下查看配置。

## 自动导入的配置

| Data ID | Group | 说明 |
|---------|-------|------|
| gov-platform-common.yaml | DEFAULT_GROUP | 共享配置：Sentinel、Feign 超时等 |
| gov-sentinel-flow-rules.json | DEFAULT_GROUP | Sentinel 流控规则 |

> **注意**：各微服务的独立配置（数据库、Redis 等）直接写在 `application-dev.yml` 中，不通过 Nacos 管理。只有需要跨服务共享的通用配置才放在 Nacos。
> 如果未来需要将某个配置迁移到 Nacos 以支持动态刷新，直接在 Nacos 控制台创建对应 Data ID 即可。

## 手动重新初始化

如果 Nacos 数据丢失需要重新初始化：

```bash
# 停止并删除 Nacos 容器和数据卷
docker-compose down -v nacos nacos-init
# 重新启动（会自动触发初始化）
docker-compose up -d nacos nacos-init
```

## 文件夹说明

| 文件 | 用途 |
|------|------|
| `init-nacos.sh` | Nacos API 自动初始化脚本 |
| `init-namespace.sql` | 通过 MySQL 直接创建命名空间（备用方案） |
| `conf/gov-platform-common.yaml` | 共享配置模板（Sentinel、Feign 等） |
| `conf/gov-sentinel-flow-rules.json` | Sentinel 限流规则 |

## 注意事项

- 配置文件中的 Sentinel Dashboard 地址（`localhost:8858`）和 Nacos 地址（`localhost:8848`）适用于 Windows 宿主机运行 Java 服务的场景
- 如果 Java 服务在 WSL2 内部运行，需要将 `localhost` 改为 `host.docker.internal` 或实际 IP
- 修改 `conf/` 下的配置文件后，需要重新初始化 Nacos 才能生效
- 运行时修改配置：直接在 Nacos 控制台编辑，微服务会自动热刷新
