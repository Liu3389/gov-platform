---
alwaysApply: false
description: Service/Mapper层编码规范。当编写业务逻辑、数据库查询、事务管理时应用此规则。涵盖@Transactional、LambdaQueryWrapper、逻辑删除过滤、异常处理、分页查询。
---
# Service / Mapper 层编码规则

## Service 规范

```java
// ✅ Service 接口
public interface ItemService extends IService<ItemEntity> {
    ItemEntity getByItemCode(String itemCode);
    IPage<ItemEntity> pageQuery(Long pageNum, Long pageSize, String keyword);  // 返回 Entity，Controller 层转 VO
}

// ✅ Service 实现
@Service
@RequiredArgsConstructor
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemEntity> implements ItemService {

    private final ItemMapper itemMapper;

    @Override
    public ItemEntity getByItemCode(String itemCode) {
        return this.lambdaQuery()
                .eq(ItemEntity::getDeleted, 0)
                .eq(ItemEntity::getItemCode, itemCode)
                .one();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ItemEntity entity) {
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ItemEntity entity) {
        return super.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        return super.removeById(id);
    }
}
```

## 数据库查询规范

```java
// ✅ LambdaQueryWrapper（类型安全）
List<ItemEntity> list = this.lambdaQuery()
    .eq(ItemEntity::getDeleted, 0)          // 必须过滤逻辑删除
    .eq(ItemEntity::getStatus, 1)
    .like(StringUtils.isNotBlank(keyword), ItemEntity::getItemName, keyword)
    .orderByDesc(ItemEntity::getCreateTime)
    .page(new Page<>(pageNum, pageSize))
    .getRecords();

// ✅ LambdaUpdateWrapper
this.lambdaUpdate()
    .eq(ItemEntity::getId, id)
    .set(ItemEntity::getStatus, 0)
    .update();

// ❌ 禁止字符串拼接
// .eq("deleted", 0)  ← 字段名可能拼错
// .eq("status", 1)
```

## 铁律

1. **所有查询必须加 `.eq(Entity::getDeleted, 0)`** —— 逻辑删除过滤
2. **使用 LambdaQueryWrapper / LambdaUpdateWrapper** —— 禁止字符串字段名
3. **所有写操作（save/update/remove）必须加 `@Transactional(rollbackFor = Exception.class)`**
4. **条件查询加 null/empty 判断** —— `.like(StringUtils.isNotBlank(x), ...)`
5. **分页查询必须用 `Page`** —— 返回 `IPage<Entity>`，Controller 层转为 `PageResult<VO>`
6. **使用 Hutool BeanUtil 或 MapStruct 做 Entity ↔ DTO/VO 转换**
7. **禁止使用 `@Autowired`** → 使用 `@RequiredArgsConstructor` + `final`
8. **禁止 try-catch 后吞掉异常** —— 要么处理，要么向上抛
9. **返回 null 改为抛异常** —— 用 `BusinessException(404, "xxx不存在")`

## 异常处理

```java
// ✅ 用 BusinessException
if (entity == null) {
    throw new BusinessException(404, "数据不存在");
}
if (StringUtils.isBlank(dto.getPhone())) {
    throw new BusinessException(400, "手机号不能为空");
}

// ❌ 禁止
// throw new RuntimeException("...")     → 会被包装成 500
// return null;                          → 调用方 NPE
```
