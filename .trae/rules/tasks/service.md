---
alwaysApply: false
description: Service/Mapper层编码规范。当编写业务逻辑、数据库查询、事务管理时应用此规则。涵盖@Transactional、LambdaQueryWrapper、逻辑删除过滤、异常处理、分页查询。
---
# Service / Mapper 层编码规则

## Service 规范

```java
// ✅ Service 接口 —— 查询返回 VO，写操作接收 DTO
public interface ItemService extends IService<ItemEntity> {
    PageResult<ItemVO> pageQueryVO(Long pageNum, Long pageSize, String itemName, Integer itemType);
    ItemVO getVOById(Long id);
    ItemVO getVOByItemCode(String itemCode);
    void addItem(ItemDTO dto);
    void updateItem(ItemDTO dto);
    void deleteItem(Long id);
}

// ✅ Service 实现
@Service
@RequiredArgsConstructor
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemEntity> implements ItemService {

    private final ItemMapper itemMapper;

    @Override
    public PageResult<ItemVO> pageQueryVO(Long pageNum, Long pageSize, String itemName, Integer itemType) {
        // 注意：pageSize 上限由 Controller 层 @Max(100) 保证，Service 不重复校验
        LambdaQueryWrapper<ItemEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ItemEntity::getDeleted, 0);
        wrapper.like(StringUtils.isNotBlank(itemName), ItemEntity::getItemName, itemName);
        wrapper.eq(itemType != null, ItemEntity::getItemType, itemType);
        wrapper.orderByDesc(ItemEntity::getCreateTime);
        Page<ItemEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return PageResult.of(ItemConvert.toVOList(page.getRecords()), page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public void addItem(ItemDTO dto) {
        ItemEntity entity = ItemConvert.toEntity(dto);
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateItem(ItemDTO dto) {
        ItemEntity entity = this.getById(dto.getId());
        if (entity == null) {
            throw new BusinessException(404, "事项不存在");
        }
        BeanUtil.copyProperties(dto, entity, "id", "itemCode", "createTime", "createBy", "deleted");
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteItem(Long id) {
        ItemEntity entity = this.getById(id);
        if (entity == null) {
            throw new BusinessException(404, "事项不存在");
        }
        this.removeById(id);
    }
}
```

## 分层职责

| 层 | 职责 | 示例 |
|----|------|------|
| Controller | 参数校验（`@Valid`/`@Max`）、接收 DTO、返回 VO、`@RequirePermission` | `pageSize` 加 `@Max(100)` |
| Service | 业务逻辑、DTO→Entity 转换、Entity→VO 转换、`@Transactional` | `pageQueryVO()` 内部查询 |
| Mapper | 数据库访问（MyBatis-Plus 自动生成） | 继承 `BaseMapper<XxxEntity>` |

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
