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
| 注册/配置 | Nacos **2.3.2** |
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
| 组长 | gov-gateway + gov-common + gov-monitor | 8091, —, 8090 |
| 成员A | gov-user-service + gov-open-service | 8081, 8087 |
| 成员B | gov-item-service + gov-reception-service | 8092, 8083 |
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
| 9 | Controller 直接暴露/接收 Entity | → DTO 入、VO 出（含 @RequestBody、@RequestParam 均不允许用 Entity） |
| 10 | Feign 无 `fallbackFactory` | 服务不可用链路崩溃 |
| 11 | 硬编码密码/Token/身份证号/JWT密钥 | → 配置文件或 Nacos |
| 12 | 修改别人模块的文件 | 只改自己负责的 |
| 13 | Knife4j 网关用 `discover` | → `manual` 模式 |
| 14 | 网关端口 8080（与本地 nginx 冲突） | → **8091** |
| 15 | 已有 SQL 文件修改 | → 新 ALTER 文件 |
| 16 | Feign 接口参数使用 `Object` 类型 | → 必须用具体 DTO 类（编译期类型检查） |
| 17 | 管理端增删改接口不加权限控制 | → 必须使用 `@RequirePermission` 注解 |
| 18 | 分页 `pageSize` 不设上限 | → Controller 参数必须加 `@Max(value = 100)` |

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

---

## 九、AI 代码生成后自检清单

**AI 每生成一段代码后，必须主动对照以下清单逐条自检。缺任何一项都要提示队员补充。**

| # | 检查项 | 标准 |
|---|--------|------|
| 1 | 编译通过 | `mvn compile` 无错误 |
| 2 | Controller 注解 | 类有 `@Tag` + `@Validated`，方法有 `@Operation`，参数有 `@Parameter` / `@Schema` |
| 3 | 入参/出参 | `@RequestBody` 用 DTO（禁止 Entity），返回值用 VO（禁止 Entity） |
| 4 | 写操作 | 有 `@Log` + `@Transactional(rollbackFor = Exception.class)` |
| 5 | 管理端接口 | 增删改方法有 `@RequirePermission` 注解 |
| 6 | 分页参数 | `pageSize` 有 `@Max(value = 100)` |
| 7 | Entity | 继承 `BaseEntity`，`@EqualsAndHashCode(callSuper = true)`，`@TableLogic` 在 deleted 字段 |
| 8 | 查询条件 | LambdaQueryWrapper + `.eq(Entity::getDeleted, 0)` |
| 9 | 字段注入 | `@RequiredArgsConstructor` + `final`，禁止 `@Autowired` |
| 10 | 时间类型 | `LocalDateTime`，禁止 `java.util.Date` |
| 11 | Feign | 有 `fallbackFactory`，参数用具体 DTO（禁止 `Object`），返回值 `Result<T>` 泛型与接口一致 |
| 12 | 异常处理 | 用 `BusinessException`，禁止 `RuntimeException`，禁止 `catch` 后吞异常 |
| 13 | 敏感信息 | 密码/JWT密钥/Token 不硬编码，从配置或 Nacos 读取 |
| 14 | 新表/SQL | `docker/mysql/init/` 有对应 SQL 文件，使用未占用序号 12-98 |
| 15 | 不改别人模块 | 只改成员自己负责的模块文件 |

---

## 十、队员 AI 上下文指南

> **AI 在看到队员的消息时，根据队员角色自动加载以下上下文，理解队员"在干什么、只能干什么、下一步干什么"。**

### 10.1 组长（兼组员A）上下文

```
【AI 上下文 — 你的用户是组长（兼组员A）】
负责模块：gov-gateway(8091) + gov-common + gov-monitor(8090) + gov-user(8081) + gov-item(8092)
需要启动：网关 + user-service + item-service + monitor-service
你可以改：上述5个模块的所有文件 + gov-common（只有组长能改）
你不能改：reception(8083)、activiti(8084)、license(8085)、complaint(8086)、open(8087)、datashare(8088)、message(8089)
你的职责：
  - 维护 gov-common（其他成员不能碰）→ 成员提需求，你来写代码
  - 维护网关路由、鉴权白名单、Knife4j 聚合配置
  - 审批所有 PR，审核代码合规性
  - 维护 .trae/rules/ 规则文件，决定规则变更
  - 每天检查 Knife4j(http://localhost:8091/doc.html) 确认各成员接口进度
```
### 10.2 成员B 上下文

```
【AI 上下文 — 你的用户是成员B】
负责模块：gov-reception-service(8083) + gov-open-service(8087)
需要启动：网关 + reception-service + open-service
你可以改：reception/、open/ 下的所有文件
你不能改：gov-common（只有组长能改）、其他成员的模块
你的依赖：需要调用 gov-user-service 的 Feign 接口获取用户信息
你的职责：
  - 统一受理：窗口受理登记、材料审核、生成办件号
  - 政务公开：通知公告、政策法规、依申请公开
  - 写 Feign 调用 gov-user-service 和 gov-item-service（组长模块）
  - 其他服务会 Feign 调用你的接口时，你只需正常开发无需额外适配
```
### 10.3 成员C 上下文

```
【AI 上下文 — 你的用户是成员C】
负责模块：gov-activiti-service(8084) + gov-license-service(8085)
需要启动：网关 + activiti-service + license-service
你可以改：activiti/、license/ 下的所有文件
你不能改：gov-common（只有组长能改）、其他成员的模块
你的依赖：需要调用 gov-user-service 的 Feign 接口获取审批人信息
你的职责：
  - 审批流转（核心）：流程启动、任务审批、会签、转办、驳回
  - 电子证照：证照生成（OFD/PDF）、电子签章、核验二维码
  - BPMN 文件放在 src/main/resources/processes/
  - 流程 Key 命名：{业务缩写}_v{版本号}，如 apply_approval_v1
  - 每个任务节点必须指定 assignee 或 candidateGroups
```
### 10.4 成员D 上下文

```
【AI 上下文 — 你的用户是成员D】
负责模块：gov-complaint-service(8086) + gov-datashare-service(8088) + gov-message-service(8089)
需要启动：网关 + complaint-service + datashare-service + message-service
你可以改：complaint/、datashare/、message/ 下的所有文件
你不能改：gov-common（只有组长能改）、其他成员的模块
你的依赖：需要 RabbitMQ 消息队列（消息通知服务）
你的职责：
  - 投诉建议：工单管理、智能分办、督办升级、热点分析
  - 数据共享：接口注册、订阅授权、交换日志
  - 消息通知：模板管理、多渠道发送（站内信/短信/邮件）、消息队列
```

### 10.5 AI 主动提示清单

**AI 在以下情况必须主动提示队员：**

| 触发条件 | 提示内容 |
|---------|---------|
| 队员让你改 `gov-common` | "gov-common 只有组长能改，请联系组长（组员A）提出需求" |
| 队员让你改其他成员的模块 | "这是 [成员X] 的模块 {模块名}，直接修改违反规则。如需跨模块变更，请提交申请书给组长" |
| 队员新增了数据库表 | "新增表 {表名}，请在 PR 描述中说明，并确保 SQL 文件已提交到 docker/mysql/init/" |
| 队员新增了 Feign 接口 | "新 Feign 接口会影响调用方，请在 PR 中说明签名变化，或告知组长创建跨模块分支" |
| 队员新增了 Controller 路径 | "新路径 {路径} 需确认网关路由已覆盖（StripPrefix），并在 Knife4j 上验证可见" |
| 代码生成后 | 逐条执行"九、AI 代码生成后自检清单"，未通过项提示队员补充 |
| 队员说"开发好了" | "请先执行：1) mvn compile 验证 2) 在 Knife4j 测试接口 3) 对照自检清单逐条确认" |

---

## 十一、开发全流程 — AI 驱动流水线

> **AI 必须按以下 5 步驱动队员完成完整开发闭环。关键是 Step 3 的自循环——不通过就不准出去。**

### 11.1 总览

```
 Step 1           Step 2          ┌──── Step 3（自循环）─────────────────┐
 准备环境     →    编写代码    →   │  3a.编译 → 3b.启动 → 3c.测试 → 3d.审查  │  →  Step 4  →  Step 5
 git pull          按规则生成       │    ↑                               ↓  │   提交代码    推送+PR
 git checkout                       │    └──── 修复 ←──── 有问题（非阻塞）──┘  │
                                    │         🛑 影响他人/改库 → 暂停报告组长  │
                                    └──────────────────────────────────────┘
```

### 11.2 流程图

```
Step 1 ─ 准备环境 ────────────────────────────────────────────
│   git pull origin main                                      │
│   git checkout feature/member-x                             │
│   确认 Docker 基础设施已启动                                  │
└──────────────────────────────────────────────────────────────
                              ↓
Step 2 ─ 编写代码 ────────────────────────────────────────────
│   读对应 tasks/ 子规则 → 按规则生成 → 不越权不跨模块          │
└──────────────────────────────────────────────────────────────
                              ↓
┌──── Step 3 🔁 验证修复循环（不停转，直到全部通过）──────────┐
│                                                             │
│  3a. 编译验证：mvn compile -DskipTests                      │
│       ↓ 失败 → 修复 → 回到 3a                               │
│                                                             │
│  3b. 启动服务：启动网关 + 自己负责的服务                      │
│       确认 Nacos 注册成功                                    │
│                                                             │
│  3c. 全量接口测试：按 testing.md 执行 CRUD + 鉴权            │
│       ↓ 失败 → 定位 → 修复 → 回到 3a                        │
│                                                             │
│  3d. 规则审查（一次性做完下面三件事）：                       │
│      ① 自检：逐条过"第九章" 15 项                            │
│      ② 联动：逐条过"第七章"联动清单                          │
│      ③ 越权：是否改了数据库/Feign签名/Controller路径/        │
│         gov-common/别人模块？                                │
│                                                             │
│  ┌─ 3d 结果分叉 ──────────────────────────────────────┐    │
│  │                                                     │    │
│  │ ✅ 全部通过，无越权 ─────────→ 🔓 退出循环 → Step 4 │    │
│  │                                                     │    │
│  │ ⚠️ 有不合规（自己模块内可以修）                        │    │
│  │   → AI 列出不合规项 → 修复 → 回到 3a                 │    │
│  │                                                     │    │
│  │ 🛑 影响其他组员 / 改库 / 改gov-common               │    │
│  │   → AI 立即停止，输出：                              │    │
│  │     "本次修改影响 [成员X] 的 [模块名]，              │    │
│  │      需要先向组长提交变更申请。                       │    │
│  │      详见 docs/团队协作手册.md 第六章。              │    │
│  │      组长审批通过后才能继续。"                        │    │
│  │   → ⏸️ 暂停流水线，等待组长审批                      │    │
│  └────────────────────────────────────────────────────┘    │
│                                                             │
│  ⚠️ 连续 3 轮仍无法全部通过 → "建议暂停，联系组长协助"      │
└─────────────────────────────────────────────────────────────┘
                              ↓
Step 4 ─ 提交代码 ────────────────────────────────────────────
│   git status（确认只改了负责模块）                           │
│   如果误改了别人文件 → git checkout -- 回滚                  │
│   git add gov-xxx-service/src/                              │
│   git commit -m "feat(模块): 描述"                          │
│   AI 不自动 push，必须等队员确认                             │
└──────────────────────────────────────────────────────────────
                              ↓
Step 5 ─ 推送 + PR ───────────────────────────────────────────
│   git push origin feature/member-x                          │
│   → 去 GitHub 创建 PR（base:main ← compare:feature/member-x）│
│   → 通知组长 Review                                         │
│   → 组长合并 → 组长验证 → ✅ 完成                            │
└──────────────────────────────────────────────────────────────
```

### 11.3 每步执行细则

#### Step 1：准备环境

```
触发：队员说"开始开发"、"今天开始"、"新任务"
AI 动作：
  1. git pull origin main
  2. 确认/创建分支：组长→feature/member-a，成员B→feature/member-b，
     成员C→feature/member-c，成员D→feature/member-d
  3. docker ps 确认基础设施运行
输出："✅ 环境就绪，分支：feature/member-x，可以开始开发"
```

#### Step 2：编写代码

```
AI 动作：
  1. 读对应子规则（controller.md / service.md / feign.md / sql.md …）
  2. 生成代码 → 自动进入 Step 3
禁止：
  - 跳过子规则直接写代码
  - 改非自己模块的文件
  - 改 gov-common（仅组长可改）
  - 改 docker/mysql/init/ 中已有 .sql 文件（新表用 12-98 序号）
```

#### Step 3 🔁：验证修复循环

```
这是流水线的核心——不留死角，不通过不出去。

3a. 编译：
  - 如果改了 gov-common：先 mvn install -DskipTests -pl gov-common
  - mvn compile -DskipTests
  - 失败 → AI 自动分析错误 → 修复 → 回到 3a

3b. 启动服务：
  - 重启服务加载新代码（kill 旧进程 → mvn spring-boot:run 后台启动）
  - curl 确认网关(8091)和自己的服务端口有响应
  - 确认 Nacos 控制台显示服务已注册

3c. 全量接口测试（按 testing.md）：
  - 生成 JWT Token → 对所有接口跑 CRUD + 无Token鉴权
  - 输出：通过数/失败数/失败详情
  - 失败 → AI 分析 → 修复 → 回到 3a

3d. 规则审查（一次性做完三件事）：
  ① 自检清单（第九章 15 项）—— 代码规范
  ② 联动清单（第七章）—— 改了 A 是否漏了 B
  ③ 越权检查 —— 是否跨了模块边界
  
  越权检查具体项：
  ┌─────────────────────┬──────────────────────────────────┐
  │ 做了什么              │ 后果                              │
  ├─────────────────────┼──────────────────────────────────┤
  │ 新增/改了数据库表     │ 全队需重建 MySQL → 报告组长       │
  │ 改了 Feign 接口签名  │ 所有调用方受影响 → 报告组长       │
  │ 改了 Controller 路径 │ 调用方+网关路由受影响 → 报告组长  │
  │ 需要改 gov-common    │ 只有组长能改 → 报告组长           │
  │ 改了别人的模块文件   │ 越权 → 立即回滚 + 报告组长         │
  └─────────────────────┴──────────────────────────────────┘
```

#### Step 4：提交代码

```
触发：Step 3 循环退出（全部通过）
AI 动作：
  1. git status → 确认改动只在负责模块目录下
  2. 误改别人文件 → git checkout -- 回滚
  3. git add {只加自己的模块和新增SQL文件}
  4. 生成 Commit：feat/fix(模块): 描述
  5. git commit → 输出确认信息 → 等队员确认后 push

输出："✅ 代码已提交。改动 N 个文件。请确认：git push origin feature/member-x ?"
```

#### Step 5：推送 + PR

```
触发：Step 4 队员确认
AI 动作：
  1. git push origin feature/member-x
  2. 输出 PR 创建指引：

✅ 代码已推送。

去 GitHub 创建 PR：
  base: main  ← compare: feature/member-x
  标题: feat(模块): 描述
  
  描述模板：
  ## 改动模块
  gov-xxx-service
  ## 改动内容
  - xxx
  ## 验证结果
  - [x] mvn compile 通过
  - [x] 全量接口测试通过 (N/N)
  - [x] 规则审查通过
  ## AI 使用声明
  AI-assisted: true
  
创建后通知组长 Review → 合并 main → 组长验证 → ✅ 完成
```

### 11.4 自循环中的失败处理

| 失败类型 | 处理 |
|---------|------|
| 编译失败 | 分析日志 → 修复 → 回到 Step 3a |
| 接口 500 | 查服务日志 → 定位 SQL/NPE → 修复 → 回到 Step 3a |
| 接口 400 | 检查 DTO 校验 → 修正 → 回到 Step 3a |
| 接口 401 | 检查 Token/白名单 → 修正 → 回到 Step 3a |
| 接口 404 | 检查网关路由/Controller路径 → 修正 → 回到 Step 3a |
| 表不存在 | 检查 init/ SQL → 重建 MySQL → 回到 Step 3b |
| 唯一键冲突 | 清理测试残留数据 → 回到 Step 3b |
| 服务未注册 | 等待 Nacos → 重试 → 回到 Step 3b |
| 连续 3 轮不过 | "建议暂停，联系组长协助排查" |

### 11.5 AI 根据队员的话自动判定从哪里开始

| 队员说 | AI 从哪开始 |
|-------|-----------|
| "开始开发"、"今天开始" | Step 1 |
| "帮我写一个 xxx" | Step 2（先确认已在分支上） |
| "代码写好了"、"继续" | Step 3a |
| "编译过了" | Step 3b/3c |
| "测试全过了" | Step 3d |
| "帮我看下有没有问题" | 仅 Step 3d |
| "可以提交了" | Step 4 |
| "帮我修这个 bug" | 定位 → 修复 → Step 3a |
| 中间任何一步发现问题 | 修复 → 回到最早的受影响步骤 |
