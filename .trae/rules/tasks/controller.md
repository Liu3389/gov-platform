---
alwaysApply: false
description: Controller层编码规范。当新建/修改Rest接口、编写请求处理、定义API路径时应用此规则。涵盖@Tag/@Operation/@Parameter注解、Result返回包装、路径命名、DTO/VO分层。
---
# Controller 层编码规则

## 接口规范

```java
@Tag(name = "事项管理", description = "事项信息管理接口")
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "分页查询事项")
    @GetMapping("/list")
    public Result<PageResult<ItemVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        // 业务参数校验在 Controller，业务逻辑在 Service
        return Result.success(itemService.pageQuery(pageNum, pageSize, keyword));
    }

    @Operation(summary = "根据ID查询事项")
    @GetMapping("/{id}")
    public Result<ItemVO> getById(@Parameter(description = "事项ID") @PathVariable Long id) {
        ItemEntity entity = itemService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("事项不存在");
        }
        return Result.success(ItemConvert.INSTANCE.toVO(entity));
    }

    @Operation(summary = "新增事项")
    @Log(module = "事项管理", action = "新增事项")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ItemDTO dto) {
        itemService.save(dto.toEntity());
        return Result.success();
    }

    @Operation(summary = "修改事项")
    @Log(module = "事项管理", action = "修改事项")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ItemDTO dto) {
        ItemEntity exist = itemService.getById(dto.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("事项不存在");
        }
        itemService.updateById(dto.toEntity());
        return Result.success();
    }

    @Operation(summary = "删除事项")
    @Log(module = "事项管理", action = "删除事项")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "事项ID") @PathVariable Long id) {
        itemService.removeById(id);
        return Result.success();
    }
}
```

## 路径规范

| 操作 | 方法 | URL | 示例 |
|------|------|-----|------|
| 分页查询 | GET | `/{resource}/list` | `/item/list` |
| 根据ID查询 | GET | `/{resource}/{id}` | `/item/1001` |
| 新增 | POST | `/{resource}` | `/item` |
| 修改 | PUT | `/{resource}` | `/item` |
| 删除 | DELETE | `/{resource}/{id}` | `/item/1001` |
| 批量删除 | DELETE | `/{resource}/batch` | `/item/batch` |

## 铁律

1. 类上必须加 `@Tag(name = "xxx")` + `@RequiredArgsConstructor`
2. 方法上必须加 `@Operation(summary = "xxx")`
3. 参数上必须加 `@Parameter(description = "xxx")` 或入参 DTO 用 `@Schema`
4. 返回值必须显式写 `Result<T>` 或 `Result<PageResult<T>>`
5. 新增/修改/删除操作必须加 `@Log(module = "xxx", action = "xxx")`
6. 入参加 `@Valid` 开启校验
7. 入参必须是 DTO（禁止直接用 Entity），出参必须是 VO（禁止返回 Entity）
8. 禁止在 Controller 中写业务逻辑——只做参数校验、类型转换、结果返回
9. 资源名用小写，动作通过 HTTP Method 表达
10. `@RequestMapping` 不包含版本号（版本号在网关 StripPrefix 处理）

## 禁止

- ❌ `@Autowired` → 用 `@RequiredArgsConstructor` + `final`
- ❌ 直接暴露 Entity → 必须转换 VO
- ❌ 在 URL 中写动词（如 `/item/create`）
