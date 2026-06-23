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

---

## 必备字段（所有业务表必须包含）

```sql
`id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
`create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
`create_by` BIGINT COMMENT '创建人ID',
`update_by` BIGINT COMMENT '更新人ID',
`deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
```

- `id`：MyBatis-Plus `IdType.ASSIGN_ID`（雪花算法）
- `deleted`：`@TableLogic` 逻辑删除
- 时间字段：`DATETIME`，默认 `CURRENT_TIMESTAMP`

## 命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 表名 | `t_` + 业务名（下划线） | `t_user_info`、`t_item_base` |
| 字段名 | 小写 + 下划线 | `user_name`、`apply_no` |
| 索引 | `idx_` + 字段名 | `idx_user_id`、`idx_status` |
| 唯一索引 | `uk_` + 字段名 | `uk_username`、`uk_item_code` |

> 表名不需要服务前缀（`t_user_info` 而非 `t_user_user_info`），因为每个服务独立数据库。

## 字段类型速查

| 用途 | MySQL 类型 | 说明 |
|------|------------|------|
| 主键 | `BIGINT` | 雪花 ID |
| 状态/枚举 | `TINYINT` | 注释必须说明每个值含义：`COMMENT '状态：0待审 1通过 2驳回'` |
| 名称 | `VARCHAR(50~100)` | 用户名、事项名 |
| 编码 | `VARCHAR(20~50)` | 唯一编码，加唯一索引 |
| 长文本/JSON | `TEXT` | 内容、配置 |
| 金额 | `DECIMAL(10,2)` | **禁止 FLOAT** |
| 手机号 | `VARCHAR(20)` | 支持国际号码 |
| 身份证 | `VARCHAR(100)` | 加密存储 |
| 时间 | `DATETIME` | 默认 `CURRENT_TIMESTAMP` |

## 敏感字段标注（必须）

```sql
`id_card` VARCHAR(100) COMMENT '身份证号 [脱敏][SM4加密存储]',
`phone` VARCHAR(20) COMMENT '手机号 [脱敏][展示时掩码138****8000]',
`password` VARCHAR(100) COMMENT '密码 [脱敏][BCrypt哈希]',
```

缺少 `[脱敏]` 标注 = 安全审查驳回。

## 索引设计原则

1. **最左前缀**：联合索引 `(a, b, c)` 支持 `a`、`a+b`、`a+b+c` 查询
2. **数量限制**：单表索引不超过 5 个
3. **区分度优先**：联合索引中区分度高的字段放前面
4. **必须加索引**：唯一编码（UNIQUE）、外键关联（INDEX）、状态查询、时间范围查询

## 建表模板

```sql
CREATE TABLE IF NOT EXISTS `t_xxx` (
  `id` BIGINT NOT NULL COMMENT '主键（雪花ID）',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT COMMENT '创建人ID',
  `update_by` BIGINT COMMENT '更新人ID',
  `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0正常 1删除',
  -- 业务字段 --
  `xxx_code` VARCHAR(50) NOT NULL COMMENT '编码（唯一）',
  `xxx_name` VARCHAR(100) NOT NULL COMMENT '名称',
  `status` TINYINT DEFAULT 0 COMMENT '状态：0待审 1通过 2驳回',
  -- 索引 --
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_xxx_code` (`xxx_code`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表说明';
```

## ALTER TABLE 示例

```sql
-- 新增字段
ALTER TABLE `t_xxx` ADD COLUMN `nickname` VARCHAR(50) COMMENT '昵称' AFTER `real_name`;
-- 新增索引
ALTER TABLE `t_xxx` ADD INDEX `idx_nickname` (`nickname`);
-- 修改字段（谨慎）
ALTER TABLE `t_xxx` MODIFY COLUMN `phone` VARCHAR(30) COMMENT '手机号 [脱敏]';
```

## 常见错误

| 错误 | 正确做法 |
|------|----------|
| `id INT AUTO_INCREMENT` | `id BIGINT NOT NULL`（雪花ID） |
| 缺少 `deleted` 字段 | 必须加 `deleted TINYINT DEFAULT 0` |
| 手动用 Navicat 改表 | 写 ALTER TABLE SQL 并提交 |
| 敏感字段无标注 | 加 `[脱敏]` 注释 |
| 使用 `java.util.Date` | 使用 `LocalDateTime` |
| 修改已有 SQL 文件 | 新建 ALTER 文件 |

> 完整规范详见 `docs/00-项目文档/数据库开发规范.md`
