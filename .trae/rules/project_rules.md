---
alwaysApply: true
description: 智慧政务一体化便民服务平台主规则。所有AI任务都必须遵循的通用规范，包括技术栈、模块分工、禁止清单、环境信息。具体编码任务需额外加载tasks/下对应子规则。
---
# 智慧政务一体化便民服务平台 — AI 开发规则

> **适用：Trae / Cursor / Copilot（自动注入）+ 网页版 AI（复制粘贴）**

---

## 任务路由表（AI 必须先读对应子规则）

| 你要做的事 | 必须先读的规则文件 |
|-----------|-------------------|
| 新建/修改 Controller | `.trae/rules/tasks/controller.md` |
| 新建/修改 Service + Mapper | `.trae/rules/tasks/service.md` |
| 新建/修改 Entity + DTO + VO | `.trae/rules/tasks/entity.md` |
| 新建/修改 Feign 调用 | `.trae/rules/tasks/feign.md` |
| 新建/修改 YAML 配置 | `.trae/rules/tasks/yaml.md` |
| 新建表/改表/写 SQL | `.trae/rules/tasks/sql.md` |
| API 接口测试/调试 | `.trae/rules/tasks/testing.md` |
| 网关鉴权/路由/白名单 | `.trae/rules/tasks/gateway.md` |
| 跨模块变更 | 见团队协作手册 `docs/团队协作手册.md` |

> **用法**：告诉 AI "新建一个 Controller"时，AI 应同时读取本文件 + `tasks/controller.md`。

---

## 一、技术栈（不可偏离）

| 类别 | 技术 | 版本 |
|------|------|------|
| JDK | Java | **17** |
| 框架 | Spring Boot **2.7.18** + Spring Cloud **2021.0.8** + Alibaba **2021.0.5.0** |
| 注册/配置 | Nacos **2.2.3** |
| 网关 | Spring Cloud Gateway **3.1.x** |
| ORM | MyBatis-Plus **3.5.5** |
| 连接池 | Druid **1.2.20** |
| DB | MySQL **8.0.33**（端口 3307） |
| 缓存 | Redis **7.x**（端口 6379，无密码） |
| 接口文档 | Knife4j + OpenAPI 3 **4.4.0** |
| JSON | Fastjson2 **2.0.43** |
| 工具 | Hutool **5.8.25** |
| JWT | jjwt **0.12.5** |

**全局禁止：** Fastjson1 / Swagger2注解 / `@Autowired` / `java.util.Date` / 字符串拼接SQL / `Result<?>`（Feign）

---

## 二、模块分工

| 角色 | 模块 | 端口 |
|------|------|:--:|
| 组长（兼组员A） | gov-gateway + gov-common + gov-monitor + gov-user + gov-item | 8091, —, 8090, 8081, 8092 |
| 成员B | gov-reception-service + gov-open-service | 8083, 8087 |
| 成员C | gov-activiti-service + gov-license-service | 8084, 8085 |
| 成员D | gov-complaint-service + gov-datashare-service + gov-message-service | 8086, 8088, 8089 |

**铁律：每人只提交自己模块，跨模块用 Feign。只有组长能改 gov-common。**

---

## 三、通用禁止清单

| # | 禁止 | 跨所有任务生效 |
|---|------|--------------|
| 1 | `@Autowired` 字段注入 | → `@RequiredArgsConstructor` + `final` |
| 2 | `java.util.Date` | → `LocalDateTime` |
| 3 | 字符串拼接 SQL | → LambdaQueryWrapper |
| 4 | 不用 `Result<T>` 包装返回值 | 前端无法统一处理 |
| 5 | 不写 `@Tag` / `@Operation` / `@Schema` | 接口契约必须可见 |
| 6 | 不继承 `BaseEntity` | 缺少公共字段 |
| 7 | 写操作不加 `@Transactional` | 数据一致性风险 |
| 8 | `catch` 后吞异常 | → 抛 `BusinessException` 或上抛 |
| 9 | Controller 直接暴露/接收 Entity | → DTO 入、VO 出 |
| 10 | Feign 无 `fallbackFactory` | 服务不可用链路崩溃 |
| 11 | 硬编码密码/Token/身份证号 | → 配置文件 |
| 12 | 修改别人模块的文件 | 只改自己负责的 |
| 13 | Knife4j 网关用 `discover` | → `manual` 模式 |
| 14 | 网关端口 8080（与本地 nginx 冲突） | → **8091** |
| 15 | 已有 SQL 文件修改 | → 新 ALTER 文件 |

---

## 四、环境

| 资源 | 地址 |
|------|------|
| 网关 Knife4j（所有接口） | http://localhost:8091/doc.html |
| Nacos | http://localhost:8848/nacos（nacos/nacos） |
| MySQL | 127.0.0.1:3307（root/root123456） |
| Redis | 127.0.0.1:6379（无密码） |
| RabbitMQ | 127.0.0.1:5672 / :15672（guest/guest） |

启动顺序：`docker-compose up -d` → 启动网关 → 启动自己模块

---

## 五、给 AI 发指令模板

```
【任务类型】Controller / Service / Feign / Entity / SQL / YAML
【项目规则】.trae/rules/project_rules.md + .trae/rules/tasks/{对应任务}.md
【我负责的模块】gov-xxx-service（端口：xxxx）
【需求】描述
【输入/输出】参数和返回值
请给出完整代码。
```

> Trae/Cursor/Copilot 自动注入主规则，**队员需手动告诉 AI 加读对应的 tasks/ 子规则**。

---

## 七、联动更新清单（改了 A 必须改 B）

**任何修改完成后，AI 必须主动检查是否需要联动更新以下关联项。缺一项都算未完成。**

| 你改了 | 必须同步检查/更新 |
|--------|------------------|
| 新增 Controller 接口 | Knife4j 文档（`@Tag/@Operation` 自动生成无需手动）→ 通知调用方"接口已上线" |
| 修改 Controller 路径 | 所有调用该接口的 FeignClient → 网关白名单（如果是公开接口）→ Knife4j 聚合路由 |
| 新增/修改 Feign 接口签名 | 本服务的 FallbackFactory → 所有调用方的 FeignClient 引用 |
| 新增 Entity 字段 | 对应的 DTO / VO → 建表 SQL（`docker/mysql/init/`） |
| 新增/修改 VO 字段 | 所有使用该 VO 的 Feign 调用方 → `gov-common` 编译验证 |
| 新增数据库表 | `docker/mysql/init/` 建表 SQL → Entity 类 → Mapper → Service |
| 修改数据库表字段 | `docker/mysql/init/` ALTER SQL → Entity 字段 → DTO/VO 同步 |
| 新增微服务 | 网关业务路由（StripPrefix）→ 网关 Knife4j 文档路由（RewritePath）→ Knife4j 聚合 routes 配置 → 端口分配表 |
| 修改网关鉴权白名单 | AuthFilter 代码 → 重启网关 → Knife4j 验证无需 Token 可访问 |
| 修改 `gov-common` 代码 | `mvn install -pl gov-common` → 通知全队 → 更新 `project_rules.md`（如有必要） |
| 修改 `project_rules.md` | `.cursorrules` 和 `.github/copilot-instructions.md` 符号链接自动同步，无需手动操作 |

> **铁律：AI 在完成任何代码修改后，必须逐条对照上表，主动列出需要联动更新的项，或者是表中没有的项，并询问是否一并处理。**

### 规则更新流程

当 AI 建议修改规则文件（`project_rules.md` 或任何 `tasks/*.md`）时：

```
1. AI 提出修改建议 → 队员逐条审查（AI 可能误判项目实际情况）
2. 队员确认后 → 本地修改规则文件
3. 队员在日常开发中验证新规则是否合理（至少用 2-3 次）
4. 验证通过 → 提交申请表给组长："建议更新 XX 规则，原因：XXX，验证次数：N"
5. 组长审批 → 合并规则变更
```

> **规则文件只有组长能合并到 main。队员可以在自己的 feature 分支上临时改规则做实验，但不能 PR 规则文件。**

---

## 八、通知

- 队员从零上手：`docs/队员上手开发指南.md`（10分钟读完）
- 详细协作规范、PR流程、跨模块变更申请：`docs/团队协作手册.md`
