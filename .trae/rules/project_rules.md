# 智慧政务一体化便民服务平台 — AI 开发规则

> **铁律：本文件是 AI IDE（Trae/Cursor/Copilot）的项目级 Prompt。
> 所有 AI 生成的代码必须严格遵循以下规则。不遵循 = 代码不合规 = 必须重写。**

---

## 一、项目技术栈（绝对不允许偏离）

| 类别 | 技术 | 版本 |
|------|------|------|
| JDK | Java | **17** |
| 核心框架 | Spring Boot | **2.7.18** |
| 微服务 | Spring Cloud | **2021.0.8** |
| 微服务治理 | Spring Cloud Alibaba | **2021.0.5.0** |
| 注册/配置中心 | Nacos | **2.2.3** |
| 网关 | Spring Cloud Gateway | **3.1.x** |
| ORM | MyBatis-Plus | **3.5.5** |
| 连接池 | Druid | **1.2.20** |
| 数据库 | MySQL | **8.0.33** |
| 缓存 | Redis | **7.x** |
| 工作流 | Activiti | **7.1.0.M6**（仅 gov-activiti-service） |
| 接口文档 | Knife4j (OpenAPI 3) | **4.4.0** |
| 工具类 | Hutool | **5.8.25** |
| JSON | Fastjson2 | **2.0.43** |
| 加密 | BouncyCastle | **1.77** |
| JWT | jjwt | **0.12.5** |
| 前端 | Vue 3 + Element Plus + Pinia | - |
| 文档聚合 | Knife4j Gateway | **4.4.0**（仅 gov-gateway） |

**禁止使用：**
- 禁止使用 `Fastjson1`（用 Fastjson2）
- 禁止使用 `Swagger 2` 注解（用 OpenAPI 3 注解：`@Tag`、`@Operation`）
- 禁止使用 `@Autowired`（用 `@RequiredArgsConstructor` + `final` 字段）
- 禁止使用 `java.util.Date`（用 `LocalDateTime`）
- 禁止字符串拼接 SQL（用 MyBatis-Plus LambdaQueryWrapper 或 XML）
- 禁止使用 `Result<?>` 通配符作为 Feign 返回值（必须写具体类型如 `Result<Object>`）
- 禁止 Knife4j 网关使用 `discover` 模式（必须用 `manual` 模式 + 显式配置路由）

---

## 二、包结构（每个服务必须遵循）

```
com.gov.{服务名}/
├── {服务名}Application.java      ← 启动类
├── config/                         ← 本服务特有配置
├── controller/                     ← 控制器（@RestController）
├── service/                        ← 服务接口
│   └── impl/                       ← 服务实现
├── mapper/                         ← MyBatis Mapper 接口
├── entity/                         ← 数据库实体（继承 BaseEntity）
├── dto/                            ← 入参 DTO
├── vo/                             ← 出参 VO
├── feign/                          ← Feign 客户端（调其他服务）
└── enums/                          ← 本服务枚举
```

---

## 三、绝对必须遵守的编码规则

### 3.1 Controller 层

```java
// ✅ 正确写法
@Tag(name = "事项管理", description = "事项信息管理接口")
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "根据ID查询事项")
    @GetMapping("/{id}")
    public Result<ItemVO> getById(@Parameter(description = "事项ID") @PathVariable Long id) {
        ItemEntity entity = itemService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("事项不存在");
        }
        return Result.success(convertToVO(entity));
    }

    @Operation(summary = "新增事项")
    @Log(module = "事项管理", action = "新增事项")   // ← 必须加 @Log
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ItemDTO dto) {
        itemService.save(dto.toEntity());
        return Result.success();
    }
}
```

**Controller 铁律：**
1. **返回值必须用 `Result<T>` 包装** —— 虽然 GlobalResponseAdvice 会自动兜底，但必须主动写
2. **类上必须加 `@Tag(name = "xxx")`** —— Knife4j 文档分组依据
3. **方法上必须加 `@Operation(summary = "xxx")`** —— 接口契约的核心
4. **参数上必须加 `@Parameter(description = "xxx")`** —— 让其他 AI 看得懂
5. **新增/修改/删除操作必须加 `@Log` 注解** —— 自动记录操作日志
6. **入参加 `@Valid`** —— 开启 JSR-303 校验
7. **禁止在 Controller 中写业务逻辑** —— 只做参数校验和结果返回

### 3.2 Entity 层

```java
// ✅ 正确写法
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_item")
@Schema(description = "政务服务事项")
public class ItemEntity extends BaseEntity {   // ← 必须继承 BaseEntity

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "状态：1启用 0禁用")
    private Integer status;
}

// ❌ 错误写法：不继承 BaseEntity、不加 @Schema
@Data
@TableName("t_item")
public class ItemEntity {  // ← 缺少 id/createTime/deleted 等公共字段！
    private String itemName;
}
```

**Entity 铁律：**
1. **必须继承 `BaseEntity`** —— 获得 id/createTime/updateTime/deleted 等字段
2. **必须加 `@EqualsAndHashCode(callSuper = true)`**
3. **字段必须加 `@Schema(description = "xxx")`** —— 生成接口文档
4. **数据库表名下划线命名，Entity 类名驼峰命名**
5. **状态字段用 `Integer`，配合枚举类给出注释**

### 3.3 Service 层

```java
// ✅ 正确：继承 ServiceImpl
public interface ItemService extends IService<ItemEntity> {
    ItemEntity getByItemCode(String itemCode);
}

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemEntity> implements ItemService {

    @Override
    public ItemEntity getByItemCode(String itemCode) {
        return this.lambdaQuery()
                .eq(ItemEntity::getDeleted, 0)     // ← 逻辑删除条件
                .eq(ItemEntity::getItemCode, itemCode)
                .one();
    }
}
```

**Service 铁律：**
1. **Mapper 查询必须加 `.eq(Entity::getDeleted, 0)`** —— 过滤已逻辑删除的数据
2. **使用 LambdaQueryWrapper** —— 防止字段名字符串写错
3. **写操作加 `@Transactional(rollbackFor = Exception.class)`**

### 3.4 Feign 客户端

```
// ✅ 正确：有 fallbackFactory 降级
@FeignClient(name = "gov-user-service", path = "/user", fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {

    @GetMapping("/{id}")
    Result<UserVO> getById(@PathVariable("id") Long id);
}

// ✅ 降级工厂：返回降级结果
@Slf4j
@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        log.error("[Feign] 调用失败", cause);
        return id -> Result.fail(503, "用户服务暂不可用");
    }
}
```

**Feign 铁律：**
1. **必须配置 `fallbackFactory`** —— 服务不可用时降级，不能直接抛异常
2. **Feign 返回值必须是 `Result<T>`** —— 保持统一格式
3. **`@PathVariable` 必须显式写 `("id")`** —— 否则编译可能失败
4. **用户上下文（X-User-Id）会自动透传** —— 无需手动在 Feign 参数中传递
5. **🚨 FallbackFactory 泛型必须和 Feign 接口完全一致** —— `Result<Object>` 绝不能写成 `Result<?>`，通配符不兼容具体类型，会导致编译错误"未覆盖抽象方法"
6. **🚨 FallbackFactory 必须覆盖 Feign 接口的全部方法** —— 接口新增方法时同步更新 FallbackFactory，不要只实现部分方法

---

## 四、异常处理规则

```java
// ✅ 正确：抛 BusinessException
if (user == null) {
    throw new BusinessException(404, "用户不存在");
}

// ✅ 正确：参数校验
if (StringUtils.isBlank(dto.getPhone())) {
    throw new BusinessException(400, "手机号不能为空");
}

// ❌ 错误：直接抛 RuntimeException
if (user == null) {
    throw new RuntimeException("用户不存在");  // ← 会被全局异常处理器包装成 500
}

// ❌ 错误：返回 null
public UserEntity getUser(Long id) {
    return null;  // ← 调用方可能 NPE
}
```

**异常铁律：**
1. **业务异常抛 `BusinessException(code, message)`** —— 由 GlobalExceptionHandler 统一处理
2. **禁止直接抛 RuntimeException、Exception**
3. **禁止返回 null** —— 用 `Optional` 或抛异常
4. **禁止 try-catch 后吞掉异常** —— 要么处理，要么向上抛
5. **不要在日志中打印密码、Token、身份证号**

---

## 五、数据库操作规则

```java
// ✅ 正确：LambdaQueryWrapper
List<ItemEntity> list = itemService.lambdaQuery()
    .eq(ItemEntity::getDeleted, 0)          // ← 必须：过滤逻辑删除
    .eq(ItemEntity::getStatus, 1)
    .like(StringUtils.isNotBlank(keyword), ItemEntity::getItemName, keyword)
    .orderByDesc(ItemEntity::getCreateTime)
    .page(new Page<>(pageNum, pageSize))
    .getRecords();

// ❌ 错误：字符串拼接条件
List<ItemEntity> list = itemService.lambdaQuery()
    .eq("deleted", 0)                       // ← 字段名可能拼错
    .eq("status", 1)
    .list();
```

**数据库铁律：**
1. **必须使用 LambdaQueryWrapper** —— 类型安全，IDE 有提示
2. **每个查询必须加 `.eq(Entity::getDeleted, 0)`** —— 逻辑删除过滤
3. **条件查询加 null/empty 判断** —— `.like(StringUtils.isNotBlank(x), ...)`
4. **分页查询必须用 `Page`**
5. **禁止字符串拼接 SQL**
6. **建表 SQL 必须同步提交到 `docker/mysql/init/`**

---

## 六、接口路径规范

| 操作 | 方法 | URL | 示例 |
|------|------|-----|------|
| 分页查询 | GET | `/{resource}/list` | `/item/list` |
| 根据ID查询 | GET | `/{resource}/{id}` | `/item/1001` |
| 新增 | POST | `/{resource}` | `/item` |
| 修改 | PUT | `/{resource}` | `/item` |
| 删除 | DELETE | `/{resource}/{id}` | `/item/1001` |
| 批量删除 | DELETE | `/{resource}/batch` | `/item/batch` |

**路径铁律：**
1. **资源名用小写** —— `/item` 而非 `/Item`、`/items`
2. **动作通过 HTTP Method 表达** —— 不在 URL 中写动词
3. **网关统一前缀 `/api/v1`** —— 由网关 StripPrefix 处理
4. **Controller 的 `@RequestMapping` 不包含版本号** —— 如 `/user` 而非 `/api/v1/user`

---

## 七、AI 开发者工作流

### 每日开发流程

```
1. 启动 docker-compose（中间件）
2. 启动自己的服务和依赖的服务
3. 打开 http://localhost:8080/doc.html 查看已有接口契约
4. 用 AI 生成代码（必须附上本规则文件作为 System Prompt）
5. 本地编译运行验证
6. 在 Knife4j 文档中测试新接口
7. Git 提交
```

### 给 AI 发指令时必须包含

```
【项目规则】见 .trae/rules/project_rules.md
【模块】gov-xxx-service
【需求】描述你要实现的功能
【输入/输出】接口参数和返回值
【参考已有代码】指出现有相似功能的路径

请给出 Controller + Service + Mapper + Entity 完整代码。
```

### Git Commit 规范

```
feat(user): 增加手机号验证码登录

- 新增 SmsCodeService 发送验证码
- 验证码存入 Redis，60秒内不可重复发送
- 登录接口新增 smsCode 参数

AI-assisted: true
```

**Commit 铁律：**
1. **AI 生成代码提交时必须标注 `AI-assisted: true`**
2. **Use Conventional Commits 格式**
3. **提交前在本地运行验证通过**
4. **不要提交 `application-local.yml` 到 Git**

---

## 八、禁止清单（AI 容易犯的错误）

| 序号 | 禁止行为 | 原因 |
|------|----------|------|
| 1 | 不用 `Result<T>` 包装返回值 | 前端无法统一处理 |
| 2 | 不写 `@Tag` / `@Operation` 注解 | 其他 AI 看不到接口契约 |
| 3 | 不写 `@Log` 注解 | 操作无法追溯 |
| 4 | 不继承 `BaseEntity` | 缺少公共字段 |
| 5 | 用 `@Autowired` 字段注入 | 改用 `@RequiredArgsConstructor` |
| 6 | 字符串拼接 SQL | 安全风险 + 字段名拼错 |
| 7 | `catch` 后吞掉异常不处理 | 问题排查困难 |
| 8 | 硬编码配置值（密码、URL等） | 改用配置文件或 Nacos |
| 9 | 在 Controller 中写业务逻辑 | 不可复用、难以测试 |
| 10 | 忘记 `.eq(Deleted, 0)` | 已删除数据被查出 |
| 11 | Feign 客户端不写 fallbackFactory | 服务不可用时整个链路崩溃 |
| 12 | 用 `java.util.Date` | 用 `LocalDateTime` |
| 13 | Feign FallbackFactory 用 `Result<?>` | 必须与接口签名严格一致用 `Result<Object>` |
| 14 | Activiti 服务忘记排除 Security 自动配置 | 启动后所有接口 401 |
| 15 | MyBatis-Plus 逻辑删除只配一半 | 同时配 `logic-delete-field` + `logic-delete-value` + `logic-not-delete-value` |
| 16 | Sentinel Dashboard 端口配成 8080 | 与 Gateway 端口冲突，改为 8858 |
| 17 | Knife4j 网关用 `discover` + StripPrefix | discover 会通过网关路由获取文档，被 StripPrefix 截断；必须用 `manual` 模式 |

---

## 九、代码审查快速检查表

合并 MR/PR 前逐条核对：

- [ ] Controller 有 `@Tag` 和 `@Operation`，参数有 `@Parameter` 或 `@Schema`
- [ ] 新增/修改操作有 `@Log` 注解
- [ ] 返回值是 `Result<T>` 或 `Result<PageResult<T>>`
- [ ] Entity 继承 `BaseEntity`，有 `@EqualsAndHashCode(callSuper = true)`
- [ ] 查询条件包含 `.eq(Deleted, 0)`
- [ ] 使用 LambdaQueryWrapper，无字符串拼接
- [ ] Feign 客户端有 `fallbackFactory`
- [ ] **Feign FallbackFactory 泛型与接口完全一致（禁止 `Result<?>`）**
- [ ] **Feign FallbackFactory 覆盖了接口全部方法**
- [ ] 异常使用 `BusinessException`，无暴力 `RuntimeException`
- [ ] MyBatis-Plus 逻辑删除三项配置齐全（`logic-delete-field` + `logic-delete-value` + `logic-not-delete-value`）
- [ ] Activiti 服务排除了 SecurityAutoConfiguration
- [ ] `mvn compile` 通过
- [ ] Knife4j 文档中新接口已出现
- [ ] 敏感信息未硬编码（密码、Token、身份证号）
- [ ] 新增数据库表在 `docker/mysql/init/` 有对应 SQL

---

## 十、每个服务 application-dev.yml 必备配置（缺一不可）

新创建服务或修改配置时，以下条目一个都不能少：

### 10.1 通用基础配置模板

```yaml
server:
  port: {分配端口}          # 端口范围 8081~8090，见下表

spring:
  application:
    name: {服务名}
  config:
    import: "optional:nacos:gov-platform-common.yaml?group=DEFAULT_GROUP"
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
        group: DEFAULT_GROUP
        username: nacos
        password: nacos
      config:
        server-addr: localhost:8848
        file-extension: yaml
        username: nacos
        password: nacos
  datasource:               # 有数据库的服务必须配（gov-gateway 除外）
    type: com.alibaba.druid.pool.DruidDataSource
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3307/{数据库名}?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: root123456
  redis:                    # 有缓存需求的服务必须配
    host: localhost
    port: 6379
    database: {分配编号}    # 每个服务独立 database 编号（见 10.3 分配表）

mybatis-plus:               # 有数据库的服务必须配
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.gov.{服务名}.entity
  global-config:
    db-config:
      id-type: assign_id
      logic-delete-field: deleted        # ← 三项缺一不可
      logic-delete-value: 1              # ←
      logic-not-delete-value: 0          # ←
  configuration:
    map-underscore-to-camel-case: true

knife4j:
  enable: true

logging:
  level:
    com.gov.{服务名}: debug
```

### 10.2 特殊服务额外必须配置

**gov-activiti-service —— 排除 Security 自动配置（否则所有接口 401）：**

```yaml
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
      - org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration

activiti:
  database-schema-update: true
  history-level: full
  async-executor-activate: true
  job-executor-activate: true
  check-process-definitions: false    # 避免启动时校验不存在的流程定义文件
```

**gov-gateway —— Knife4j 聚合（必须 manual 模式，禁止 discover）：**

```yaml
knife4j:
  gateway:
    enabled: true
    strategy: manual                   # ← 必须 manual！discover 会被 StripPrefix 截断
    routes:
      - name: 用户认证服务
        service-name: gov-user-service
        url: /user/v3/api-docs         # ← 网关路径：Knife4j 通过网关路由获取文档
        context-path: /api/v1/user     # ← Try It 调 API 时的前缀
        order: 1
      - name: 事项管理服务
        service-name: gov-item-service
        url: /item/v3/api-docs
        context-path: /api/v1/item
        order: 2
      # ... 其余服务同理
```

网关必须为每个服务配 **RewritePath** 路由（放在所有业务路由**之前**，顺序很重要）：

```yaml
spring:
  cloud:
    gateway:
      routes:
        # ===== Knife4j 文档路由（RewritePath，必须在业务路由之前） =====
        - id: knife4j-item-docs
          uri: lb://gov-item-service
          predicates:
            - Path=/item/v3/api-docs/**
          filters:
            - RewritePath=/item/(?<remaining>.*), /$\{remaining}
        # ===== 业务路由在文档路由之后 =====
        - id: gov-item-service
          uri: lb://gov-item-service
          predicates:
            - Path=/api/v1/item/**
          filters:
            - StripPrefix=2
```

> **重要**：
> - `url` 是 Knife4j 网关通过网关层获取文档的路径，必须带服务名前缀
> - 网关用 `RewritePath` 剥掉服务名，将 `/item/v3/api-docs` 改写为 `/v3/api-docs` 再转发给服务
> - 文档路由必须在业务路由**之前**，否则可能被误匹配
> - `context-path` 告诉 Knife4j 前端"Try It"时通过什么路径调用 API

### 10.3 端口与 Redis database 分配表

| 服务 | 端口 | Redis DB | 数据库名 |
|------|------|----------|----------|
| gov-gateway | 8080 | - | - |
| gov-user-service | 8081 | 1 | gov_user |
| gov-item-service | 8092 | 2 | gov_item |
| gov-reception-service | 8083 | 3 | gov_reception |
| gov-activiti-service | 8084 | 4 | gov_activiti |
| gov-license-service | 8085 | 5 | gov_license |
| gov-complaint-service | 8086 | 6 | gov_complaint |
| gov-open-service | 8087 | 7 | gov_open |
| gov-datashare-service | 8088 | 8 | gov_datashare |
| gov-message-service | 8089 | 9 | gov_message |
| gov-monitor-service | 8090 | 10 | gov_monitor |

### 10.4 Sentinel Dashboard 端口

Sentinel Dashboard **必须使用 8858 端口**，不可用 8080（与 Gateway 端口冲突）。

Nacos 共享配置 `gov-platform-common.yaml` 中：

```yaml
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8858    # ← 不可用 8080
```

---

## 十一、新增或修改 Feign 接口时的检查清单

每新增或修改一个 Feign 接口方法，**必须**同步完成以下检查：

| 序号 | 检查项 | 正确 | 错误（已踩坑） |
|------|--------|------|---------------|
| 1 | Feign 接口返回类型 | `Result<Object>` 具体类型 | `Result<?>` 通配符 |
| 2 | FallbackFactory 返回泛型 | 与 Feign 接口**逐字一致** | 接口写 `Result<Object>`，Fallback 写 `Result<?>` |
| 3 | FallbackFactory 覆盖方法 | **全部**方法都有 `@Override` | 新加了 `getByUsername()`，Fallback 却没实现 |
| 4 | `@PathVariable` 参数名 | `@PathVariable("id")` 显式写 | `@PathVariable` 省略参数名 |
| 5 | 编译验证 | `mvn compile -pl {模块名}` 通过 | 没编译就提交 |

**核心原因**：Java 编译器对泛型是严格匹配的。`Result<?>` 表示"未知类型的 Result"，`Result<Object>` 表示"Object 类型的 Result"。两者在方法签名中不兼容，编译器会报"未覆盖抽象方法"。

```java
// ❌ 错误：FallbackFactory 泛型不匹配
public interface UserFeignClient {
    Result<Object> getById(Long id);           // 接口写 Result<Object>
}
return new UserFeignClient() {
    public Result<?> getById(Long id) { ... }  // Fallback 写 Result<?> → 编译错误
};

// ✅ 正确：完全一致
return new UserFeignClient() {
    public Result<Object> getById(Long id) { ... }  // 与接口一模一样
};
```
