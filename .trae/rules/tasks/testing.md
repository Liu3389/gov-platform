---
alwaysApply: false
description: API接口测试规则。当开发完成后需要自动测试接口、验证鉴权、生成测试报告时应用此规则。涵盖Token生成、CRUD测试用例、鉴权验证、测试报告模板。
---
# API 接口测试规则

> **队员开发完成后，告诉 AI 按本规则自动测试。**

---

## 测试前准备

### 0. 编译验证（必须先跑）

```
在测试前必须执行编译：
mvn compile -pl gov-platform-parent

如果编译失败 → 报告编译错误，不继续测试
如果编译成功 → 继续以下步骤
```

### 1. 获取 JWT Token

```
用 Python 生成测试 Token（有效期 2 小时）：
密钥：GovPlatformSecretKey2024ForJwtTokenGenerationAndValidation
算法：HS256
Payload：{"sub":"1","username":"admin","iss":"gov-platform","iat":当前时间戳,"exp":当前时间戳+7200}
Header：{"alg":"HS256","typ":"JWT"}
格式：紧凑 JSON（separators=(',',':')，无空格）
```

### 2. 确认测试范围

```
通过 http://localhost:8091/v3/api-docs/swagger-config 获取所有已注册服务列表。
只测试调用方负责的模块中的服务。
```

### 3. 基路径

```
通过网关：http://localhost:8091/api/v1/{服务资源名}/{接口}
直连服务：http://localhost:{端口}/{接口}
所有请求必须带 Header：Authorization: Bearer <token>
```

---

## 标准 CRUD 测试用例（每个服务必跑）

### Test 1：分页查询 — 无参数

```
GET http://localhost:8091/api/v1/{resource}/list?pageNum=1&pageSize=2
Header: Authorization: Bearer <token>
期望：200，返回 {"code":200,"data":{"records":[...],"total":N,"pageNum":1,"pageSize":2}}
```

### Test 2：分页查询 — 带搜索参数

```
如果有搜索条件（如 username、keyword），测试带参数的查询：
GET http://localhost:8091/api/v1/{resource}/list?pageNum=1&pageSize=5&keyword=测试
期望：200
```

### Test 3：新增

```
POST http://localhost:8091/api/v1/{resource}
Header: Authorization: Bearer <token>
Content-Type: application/json
Body：一个合法的 DTO JSON（从 Knife4j 文档 Schema 中获取字段名和类型）
期望：200，{"code":200}

# 从响应中提取新资源的 ID（如果返回了）
```

### Test 4：根据 ID 查询

```
GET http://localhost:8091/api/v1/{resource}/{id}
Header: Authorization: Bearer <token>
期望：200 或 404（"数据不存在"）
```

### Test 5：修改

```
PUT http://localhost:8091/api/v1/{resource}
Header: Authorization: Bearer <token>
Content-Type: application/json
Body：{"id": 1, ...修改字段}
期望：200
```

### Test 6：删除

```
DELETE http://localhost:8091/api/v1/{resource}/{id}
Header: Authorization: Bearer <token>
期望：200
```

### Test 7：无 Token 鉴权验证

```
GET http://localhost:8091/api/v1/{resource}/list?pageNum=1&pageSize=1
不传 Authorization Header
期望：401
```

---

## 测试报告模板

测试完成后，输出以下格式的报告：

```
=== [服务名] 接口测试报告 ===

Token: <前20字符>...
测试时间: YYYY-MM-DD HH:MM:SS

| # | 接口 | 方法 | 路径 | 预期 | 实际 | 结果 |
|---|------|------|------|:--:|:--:|:--:|
| 1 | 分页查询 | GET | /{resource}/list | 200 | 200 | ✅ |
| 2 | 带搜索分页 | GET | /{resource}/list?keyword=x | 200 | 200 | ✅ |
| 3 | 新增 | POST | /{resource} | 200 | 200 | ✅ |
| 4 | ID查询 | GET | /{resource}/{id} | 200 | 200 | ✅ |
| 5 | 修改 | PUT | /{resource} | 200 | 200 | ✅ |
| 6 | 删除 | DELETE | /{resource}/{id} | 200 | 200 | ✅ |
| 7 | 无Token | GET | /{resource}/list | 401 | 401 | ✅ |

通过率：7/7 (100%)

异常详情（如有）：
- 无

结论：✅ 全部通过 / ⚠️ 有 N 项失败，详见上方
```

---

## 调用 AI 测试的指令模板

```
【任务类型】API 测试
【项目规则】.trae/rules/project_rules.md + .trae/rules/tasks/testing.md
【我负责的模块】gov-user-service（端口：8081）
【已启动服务】8091(网关) + 8081(user) + 8087(open)
【测试范围】user-service 的所有接口
【特殊要求】无

请按测试规则生成 Token 并测试我模块的所有接口，输出测试报告。
```
