# AI 编码 Prompt 模板

> 本规范适用于全体成员使用 AI（大模型、Copilot、Trae 等）辅助开发。AI 是工具，最终代码责任由开发者本人承担。

---

## 一、AI 使用原则

1. **人为主导，AI 辅助**：AI 生成草稿，开发者必须理解代码含义。
2. **可解释性**：关键逻辑必须加注释，禁止直接合并看不懂的代码。
3. **安全合规**：禁止将密码、私钥、CA 证书、真实公民数据喂给 AI。
4. **代码归属**：提交到 Git 的代码视为提交者本人负责，AI 辅助需在 Commit 或 MR 中标注 `AI-assisted`。

---

## 二、通用 Prompt 模板

### 2.1 写 Prompt 前必须提供

- 项目背景：技术栈、模块名、目标。
- 输入/输出：接口参数、返回值类型。
- 约束条件：命名规范、异常处理、安全要求。
- 示例：正确的调用示例或返回示例。

### 2.2 后端接口开发 Prompt

```text
【模块】gov-user-service
【技术栈】SpringBoot 2.7.18 + SpringCloud 2021.0.8 + SpringCloud Alibaba 2021.0.5.0 + MyBatis-Plus 3.5.5 + MySQL 8.0.33 + Knife4j 4.4.0 + Hutool 5.8.25
【需求】实现一个用户注册接口，支持手机号验证码注册
【约束】
1. 参数校验使用 JSR-303 + 自定义注解
2. 密码使用 BCrypt 加密存储
3. 返回统一响应体 Result<T>
4. 手机号需要防刷（60 秒内只能发一次）
5. Controller 必须加 Knife4j 注解（@ApiOperation、@ApiImplicitParam）
6. Service 写操作加 @Transactional
7. 不写死任何常量，使用 Hutool 工具类
【示例】
请求：POST /api/v1/users/register
{
  "phone":"13800138000",
  "smsCode":"123456",
  "password":"a12345678"
}
请给出 Controller、Service、Mapper、Entity、DTO 完整代码。
```

### 2.3 数据库设计 Prompt

```text
【模块】gov-item-service
【数据库】MySQL 8.0.33
【需求】设计“政务服务事项”表，包含事项名称、事项编码、所属部门、办理材料、状态、排序字段
【约束】
1. 表名小写下划线，以 gov_ 开头
2. 必须包含 id、create_time、update_time、create_by、update_by、deleted 字段
3. id 使用 BIGINT 雪花 ID
4. 状态字段用 TINYINT，并给出枚举说明
5. 高频查询字段加索引
请给出建表 SQL、MyBatis-Plus Entity、枚举类。
```

### 2.4 工作流 BPMN Prompt

```text
【模块】gov-activiti-service
【技术栈】Activiti 7.7.0
【需求】设计一个"在线办理审批流程"
【节点】申请人提交 → 部门初审 → 部门联合审批（会签） → 结果通知 → 结束
【约束】
1. 流程 key 命名：apply_approval_v1
2. 每个任务节点指定 candidateGroups
3. 支持初审驳回、退回申请人
4. BPMN 文件放在 src/main/resources/processes/
请给出 BPMN XML 和流程图说明。
```

### 2.5 前端页面 Prompt

```text
【模块】gov-web
【技术栈】Vue 3 + Element Plus + Axios
【需求】实现“政务服务事项列表页”
【功能】分页查询、关键字搜索、状态筛选、新增/编辑/删除按钮
【约束】
1. 使用 Element Plus 的 el-table、el-pagination
2. 请求封装在 src/api/item.js
3. 使用 Mock.js 做本地 Mock 数据
4. 按钮权限使用 v-permission 指令
请给出 Vue 单文件组件和 API 层代码。
```

---

## 三、AI 生成代码审查清单

合并前必须逐条自检：

| 检查项 | 标准 |
|--------|------|
| 编译通过 | `mvn clean compile` 无错误 |
| 单元测试 | 关键 Service 有单元测试 |
| Knife4j 文档 | Controller 加 `@ApiOperation`、参数说明、示例值 |
| 命名规范 | 类名大驼峰、方法名小驼峰、常量全大写下划线 |
| SQL 安全 | 禁止字符串拼接 SQL，必须参数化 |
| 空指针 | 对可能为 null 的入参/返回值做防御 |
| 事务 | 写操作必须加 `@Transactional` |
| 异常 | 使用业务异常，禁止直接抛 `Exception` |
| 日志 | 关键流程打印日志，禁止打印敏感信息 |
| 返回值 | 统一封装 `Result<T>` |
| 接口幂等 | 提交类接口必须做幂等 |

---

## 四、Knife4j + AI 排错 Prompt

### 4.1 通用模板

```text
【环境】SpringBoot 2.7.18 + SpringCloud 2021.0.8 + SpringCloud Alibaba 2021.0.5.0 + SpringCloud Gateway 3.1.x + Nacos 2.2.3 + Knife4j 4.4.0
【本地测试地址】http://localhost:8080/doc.html
【接口】POST /api/v1/auth/login
【请求参数】
{
  "username": "admin",
  "password": "123456"
}
【实际响应】
{
  "code": 500,
  "message": "NullPointerException in AuthServiceImpl:42"
}
【相关日志】
java.lang.NullPointerException: Cannot invoke ...
【相关代码】
{AuthServiceImpl.java 片段}

请分析可能原因并给出修复步骤。
```

### 4.2 Feign 调用失败

```text
【环境】SpringCloud 2021.0.8 + Nacos 2.2.3 + OpenFeign
【现象】gov-workflow-service 调用 gov-item-service 报 404
【被调用服务名】gov-item-service
【Feign 接口】
{FeignClient 代码}
【Nacos 服务列表截图】
{描述}
【错误日志】
{日志}
请分析可能原因。
```

### 4.3 前端接口报错

```text
【环境】Vue 3 + Element Plus + Axios
【现象】调用 /api/v1/items 返回 401
【Axios 配置】
{代码}
【浏览器 Network 截图】
{描述}
请分析原因并给出修复方案。
```

---

## 五、AI 使用场景分级

| 场景 | 推荐程度 | 说明 |
|------|----------|------|
| 生成 CRUD、DTO、Mapper | 强烈推荐 | 重复劳动 |
| 生成业务复杂 SQL | 谨慎 | 必须 Review 执行计划 |
| 设计工作流 BPMN | 辅助 | AI 画 xml，人工校验 |
| 生成加密/签名算法 | 谨慎 | 使用 BouncyCastle 等验证库 |
| 代码重构建议 | 推荐 | AI 给 diff，人工确认 |
| 报错定位 | 推荐 | 把堆栈+上下文喂给 AI |
| Knife4j 接口分析 | 推荐 | 请求/响应/日志一起给 AI |
| 直接合并 AI 完整模块 | 禁止 | 必须拆分 Review |

---

## 六、每日 AI 协作 checklist

- [ ] 今天是否保存了可用的 Prompt 模板？
- [ ] AI 生成的代码是否已本地编译/运行通过？
- [ ] 是否删除了 AI 生成的无用注释/示例代码？
- [ ] 是否对关键 AI 代码添加了人工注释？
- [ ] 是否未将敏感信息喂给 AI？
- [ ] 新接口是否已在 Knife4j 中自测通过？
