---
alwaysApply: false
description: 数据库/SQL管理规范。当建表、改表、写SQL脚本、管理db/目录时应用此规则。涵盖Flyway命名、建表模板、ALTER流程、种子数据管理。
---
# 数据库 / SQL 管理规则

## 一、Flyway 数据库版本管理

项目使用 [Flyway](https://flywaydb.org/) 管理 SQL，确保多人协作不冲突。

```
db/
├── schema/                              ← 版本化迁移（不可修改历史）
│   ├── V1.0.0__init_database.sql        ← 初始化所有数据库
│   ├── V1.0.1__gov_user_tables.sql      ← 用户模块建表（组长）
│   ├── V1.0.2__gov_item_tables.sql      ← 事项模块建表（组长）
│   ├── V1.0.3__gov_reception_tables.sql ← 受理模块建表（成员B）
│   ├── V1.0.4__gov_activiti_tables.sql  ← 审批模块建表（成员C）
│   ├── V1.0.5__gov_license_tables.sql   ← 证照模块建表（成员C）
│   ├── V1.0.6__gov_complaint_tables.sql ← 投诉模块建表（成员D）
│   ├── V1.0.7__gov_open_tables.sql      ← 公开模块建表（成员B）
│   ├── V1.0.8__gov_datashare_tables.sql ← 共享模块建表（成员D）
│   ├── V1.0.9__gov_message_tables.sql   ← 消息模块建表（成员D）
│   ├── V1.0.10__gov_monitor_tables.sql  ← 监察模块建表（组长）
│   ├── V1.1.0__reception_schema_fix.sql
│   └── V1.1.1__entity_mismatch_fix.sql
├── data/                                ← 可重复迁移（每次变更自动重跑）
│   ├── R__gov_user_seed.sql             ← 用户种子数据
│   ├── R__gov_item_seed.sql
│   ├── R__gov_reception_seed.sql
│   ├── R__gov_activiti_seed.sql
│   ├── R__gov_license_seed.sql
│   ├── R__gov_complaint_seed.sql
│   ├── R__gov_open_seed.sql
│   ├── R__gov_datashare_seed.sql
│   ├── R__gov_message_seed.sql
│   └── R__gov_monitor_seed.sql
├── flyway.conf                          ← Flyway 配置
└── reset.sh                             ← 一键重建数据库
```

## 二、Flyway 命名规范（铁律）

| 场景 | 文件名格式 | 示例 |
|------|-----------|------|
| 建表（新模块） | `V{主版本}.{次版本}.{修订号}__{模块}_{描述}.sql` | `V1.0.3__gov_reception_tables.sql` |
| 改表（加字段） | `V{新版本号}__{模块}_alter_{描述}.sql` | `V1.2.0__gov_user_alter_add_nickname.sql` |
| 种子数据 | `R__{模块}_seed.sql` | `R__gov_user_seed.sql` |

**铁律：不修改已提交的 schema SQL 文件（V版本号唯一），改表用新版本 ALTER 脚本。**

## 三、建表流程

```
1. 在 db/schema/ 下新建 Flyway V版本号 SQL
2. 使用本文档的建表模板编写 SQL
3. 本地执行 bash db/reset.sh 验证
4. 代码和 SQL 一起提交 PR
5. 组长审核 → 合并
6. 全队 bash db/reset.sh 重建
```

## 四、ALTER 表流程

```
1. 在 db/schema/ 下新建 V1.X.X__模块_alter_描述.sql
2. PR 提交
3. 全队 bash db/reset.sh 重建
```

## 五、一键重建数据库

```bash
bash db/reset.sh
```

自动执行：停止容器 → 清空数据卷 → 启动 MySQL → Flyway 迁移（schema + data）→ 启动基础设施 → Nacos 初始化。

## 六、数据库连接信息

| 配置 | 值 |
|------|-----|
| Host | 127.0.0.1 |
| Port | **3307** |
| User | root |
| Password | root123456 |

## 七、各服务数据库分配

| 服务 | 数据库名 | Redis DB | 负责人 |
|------|----------|:--:|--------|
| gov-user-service | gov_user | 1 | 组长 |
| gov-item-service | gov_item | 2 | 组长 |
| gov-reception-service | gov_reception | 3 | 成员B |
| gov-activiti-service | gov_activiti | 4 | 成员C |
| gov-license-service | gov_license | 5 | 成员C |
| gov-complaint-service | gov_complaint | 6 | 成员D |
| gov-open-service | gov_open | 7 | 成员B |
| gov-datashare-service | gov_datashare | 8 | 成员D |
| gov-message-service | gov_message | 9 | 成员D |
| gov-monitor-service | gov_monitor | 10 | 组长 |

## 八、铁律

1. 新增表但未提交 SQL = PR 不允许合并
2. **不修改已提交的 Flyway schema SQL** —— 改表用新版本 ALTER 文件
3. SQL 文件必须包含 `IF NOT EXISTS` 防止重复执行
4. 表名用 `t_` 前缀 + 下划线命名
5. 字段名用下划线命名，MySQL 用 utf8mb4
6. 所有模块共享同一 MySQL 实例（端口 **3307**）

## 九、必备字段（所有业务表必须包含）

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

## 十、命名规范

| 类型 | 规范 | 示例 |
|------|------|------|
| 表名 | `t_` + 业务名 | `t_user_info`、`t_item_base` |
| 字段名 | 小写 + 下划线 | `user_name`、`apply_no` |
| 索引 | `idx_` + 字段名 | `idx_user_id` |
| 唯一索引 | `uk_` + 字段名 | `uk_username` |

## 十一、字段类型速查

| 用途 | MySQL 类型 | 说明 |
|------|------------|------|
| 主键 | `BIGINT` | 雪花 ID |
| 状态/枚举 | `TINYINT` | 注释必须说明含义 |
| 名称 | `VARCHAR(50~100)` | |
| 编码 | `VARCHAR(20~50)` | 加唯一索引 |
| 长文本 | `TEXT` | |
| 金额 | `DECIMAL(10,2)` | 禁止 FLOAT |
| 手机号 | `VARCHAR(20)` | 脱敏标注 |
| 身份证 | `VARCHAR(100)` | SM4加密标注 |
| 时间 | `DATETIME` | DEFAULT CURRENT_TIMESTAMP |

## 十二、建表模板

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
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表说明';
```

## 十三、ALTER TABLE 示例

```sql
-- 新增字段
ALTER TABLE `t_xxx` ADD COLUMN `nickname` VARCHAR(50) COMMENT '昵称' AFTER `real_name`;
-- 修改字段（谨慎）
ALTER TABLE `t_xxx` MODIFY COLUMN `phone` VARCHAR(30) COMMENT '手机号 [脱敏]';
```

## 十四、常见错误

| 错误 | 正确做法 |
|------|----------|
| `id INT AUTO_INCREMENT` | `id BIGINT NOT NULL`（雪花ID） |
| 缺少 `deleted` 字段 | 必须加 `deleted TINYINT DEFAULT 0` |
| 修改已有 schema SQL | 新建 Flyway V版本号 ALTER 文件 |
| 敏感字段无标注 | 加 `[脱敏]` 注释 |

> 完整规范详见 `docs/00-项目文档/数据库开发规范.md`
