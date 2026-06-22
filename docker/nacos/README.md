# Nacos 配置导出说明

本目录包含 Nacos 配置中心的导出配置文件，导入后即可使用。

## 导入步骤

1. 启动 Nacos：`docker-compose up -d nacos`
2. 打开 Nacos 控制台：http://localhost:8848/nacos
3. 登录：用户名 `nacos`，密码 `nacos`
4. 创建命名空间：
   - 命名空间ID：`gov-platform`
   - 命名空间名：`智慧政务平台`
5. 导入配置：
   - 点击"配置管理" → "导入配置"
   - 选择 `nacos_config_export.zip`
   - 目标命名空间：`gov-platform`

## 配置列表

导入后将包含以下配置：

| Data ID | Group | 说明 |
|---------|-------|------|
| gov-gateway.yaml | DEFAULT_GROUP | 网关路由配置 |
| gov-user-service.yaml | DEFAULT_GROUP | 用户服务配置 |
| gov-item-service.yaml | DEFAULT_GROUP | 事项服务配置 |
| gov-reception-service.yaml | DEFAULT_GROUP | 受理服务配置 |
| gov-activiti-service.yaml | DEFAULT_GROUP | 审批服务配置 |
| gov-license-service.yaml | DEFAULT_GROUP | 证照服务配置 |
| gov-complaint-service.yaml | DEFAULT_GROUP | 投诉服务配置 |
| gov-open-service.yaml | DEFAULT_GROUP | 公开服务配置 |
| gov-datashare-service.yaml | DEFAULT_GROUP | 共享服务配置 |
| gov-message-service.yaml | DEFAULT_GROUP | 消息服务配置 |
| gov-monitor-service.yaml | DEFAULT_GROUP | 监控服务配置 |

## 注意事项

- 配置文件中的数据库密码、Redis 地址等需要根据实际环境调整
- 如需修改配置，直接在 Nacos 控制台编辑，无需重启服务（动态刷新）