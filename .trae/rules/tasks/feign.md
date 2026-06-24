---
alwaysApply: false
description: Feign远程调用规范。当编写跨服务调用、FallbackFactory降级、服务间通信时应用此规则。涵盖@FeignClient配置、降级工厂、泛型一致、路径匹配、跨服务契约流程。
---
# Feign 远程调用编码规则

## Feign 客户端

```java
@FeignClient(name = "gov-user-service", path = "/user", fallbackFactory = UserFeignFallbackFactory.class)
public interface UserFeignClient {

    @GetMapping("/{id}")
    Result<UserVO> getById(@PathVariable("id") Long id);

    @GetMapping("/byUsername")
    Result<UserVO> getByUsername(@RequestParam("username") String username);
}
```

## FallbackFactory（降级工厂）

```java
@Slf4j
@Component
public class UserFeignFallbackFactory implements FallbackFactory<UserFeignClient> {

    @Override
    public UserFeignClient create(Throwable cause) {
        log.error("[Feign] 调用 gov-user-service 失败", cause);
        return new UserFeignClient() {
            @Override
            public Result<UserVO> getById(Long id) {
                return SentinelBlockHandler.<UserVO>blockHandler("user-service", "getById");
            }

            @Override
            public Result<UserVO> getByUsername(String username) {
                return SentinelBlockHandler.<UserVO>blockHandler("user-service", "getByUsername");
            }
        };
    }
}
```

## 铁律

1. **必须配置 `fallbackFactory`** —— 服务不可用时降级
2. **返回值必须是 `Result<T>`** —— 统一格式
3. **`@PathVariable("id")` 必须显式写括号内的参数名** —— 否则编译可能失败
4. **FallbackFactory 泛型必须和 Feign 接口逐字一致** —— 禁止 `Result<?>`
5. **FallbackFactory 必须 `@Override` 接口的全部方法** —— 新增方法时同步更新
6. **Feign 路径必须与被调方 Controller 路径完全一致**
7. **用户上下文（Authorization/X-User-Id/X-Username）会自动透传** —— 无需手动传
8. **写 FeignClient 前先去 Knife4j(http://localhost:8091/doc.html) 确认被调方接口已上线**

## 跨服务调用流程

```
1. 被调方在 Knife4j 上验证接口可用
2. 被调方告知调用方"接口已就绪，路径是 /xxx/xxx"
3. 调用方写 FeignClient + FallbackFactory
4. 调用方本地启动两个服务验证
5. 任何人改 Controller 路径前必须通知所有调用方
```

## 禁止

- ❌ Feign 不写 fallbackFactory
- ❌ `Result<?>` 通配符 → 必须具体类型
- ❌ **Feign 方法参数使用 `Object` 类型** → 必须用具体 DTO 类，编译期类型检查
- ❌ FallbackFactory 方法签名与 Feign 接口不一致
- ❌ Feign 路径与被调方 Controller 路径不匹配
- ❌ 在 Feign 参数中手动透传 Token → 自动透传已处理

## Feign 接口参数示例

```java
// ✅ 正确：用具体 DTO 类型
@FeignClient(name = "gov-license-service", path = "/license", fallbackFactory = LicenseFeignFallbackFactory.class)
public interface LicenseFeignClient {
    @PostMapping("/generate")
    Result<LicenseVO> generate(@RequestBody LicenseGenerateDTO dto);
}

// ❌ 错误：用 Object 丢失类型安全
@PostMapping("/generate")
Result<LicenseVO> generate(@RequestBody Object generateDTO);  // 禁止！

// ❌ 错误：用 Map 代替 DTO
@PostMapping("/generate")
Result<LicenseVO> generate(@RequestBody Map<String, Object> params);  // 禁止！
```
