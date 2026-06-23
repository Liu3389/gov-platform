---
alwaysApply: false
description: 数据库/SQL管理规范。当建表、改表、写SQL脚本、管理docker/mysql/init/目录时应用此规则。涵盖建表文件命名、ALTER表流程、序号管理、连接信息。
---
# 数据库 / SQL 管理规则

## 建表 SQL 规范

```
docker/mysql/init/ 是唯一出处。

新增表：      新建 XX-description.sql   （XX 为未使用的序号）
修改表：      新建 XX-alter.sql         （永远不修改已有 SQL）
种子数据：    仅加必需的初始化数据       （不加测试数据）
```

**已有 SQL 文件不可修改**：因为它们只在 MySQL 容器首次创建时执行一次。

## 已占用 SQL 序号

| 文件 | 内容 |
|------|------|
| 01-init-db.sql | 创建所有数据库 |
| 02-gov_user.sql | 用户库建表 |
| 03-gov_item.sql | 事项库建表 |
| 04-gov_reception.sql | 受理库建表 |
| 05-gov_activiti.sql | 审批库建表 |
| 06-gov_license.sql | 证照库建表 |
| 07-gov_complaint.sql | 投诉库建表 |
| 08-gov_open.sql | 公开库建表 |
| 09-gov_datashare.sql | 共享库建表 |
| 10-gov_message.sql | 消息库建表 |
| 11-gov_monitor.sql | 监察库建表 |
| 99-init-data.sql | 种子数据 |

**新 SQL 文件使用 12-98 之间的序号。**

## ALTER 表流程

```
1. 在 docker/mysql/init/ 新建 XX-alter.sql（例如 13-alter-license-add-expire.sql）
2. PR 提交
3. 全队 docker-compose down && docker-compose up -d mysql 重建
```

## 数据库连接信息

| 配置 | 值 |
|------|-----|
| Host | 127.0.0.1 |
| Port | 3307 |
| User | root |
| Password | root123456 |

## 各服务数据库分配

| 服务 | 数据库名 | Redis DB |
|------|----------|:--:|
| gov-user-service | gov_user | 1 |
| gov-item-service | gov_item | 2 |
| gov-reception-service | gov_reception | 3 |
| gov-activiti-service | gov_activiti | 4 |
| gov-license-service | gov_license | 5 |
| gov-complaint-service | gov_complaint | 6 |
| gov-open-service | gov_open | 7 |
| gov-datashare-service | gov_datashare | 8 |
| gov-message-service | gov_message | 9 |
| gov-monitor-service | gov_monitor | 10 |

## 铁律

1. 新增表但未提交 SQL = PR 不允许合并
2. **永远不修改已有 SQL 文件** —— 改表用 ALTER 文件
3. SQL 文件必须包含 `IF NOT EXISTS` 防止重复执行
4. 表名用 `t_` 前缀 + 下划线命名
5. 字段名用下划线命名，MySQL 用 utf8mb4
6. 所有模块共享同一 MySQL 实例（端口 3307）
