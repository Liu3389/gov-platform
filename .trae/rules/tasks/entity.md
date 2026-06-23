---
alwaysApply: false
description: Entity/DTO/VO分层规范。当新建/修改数据库实体、入参DTO、出参VO时应用此规则。涵盖BaseEntity继承、@Schema注解、DTO→Entity转换、VO跨服务共享规则。
---
# Entity / DTO / VO 层编码规则

## Entity 规范

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_item")
@Schema(description = "政务服务事项")
public class ItemEntity extends BaseEntity {

    @Schema(description = "事项名称")
    private String itemName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "状态：1启用 0禁用")
    private Integer status;
}
```

## DTO 规范（入参，每个服务自己的 dto/ 包）

```java
@Data
@Schema(description = "新增事项请求")
public class ItemDTO {
    @NotBlank(message = "事项名称不能为空")
    @Schema(description = "事项名称", required = true)
    private String itemName;

    @Schema(description = "所属部门ID")
    private Long deptId;

    @Schema(description = "状态")
    private Integer status;

    /** 转为 Entity，供 Service 层使用 */
    public ItemEntity toEntity() {
        ItemEntity entity = new ItemEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }
}
```

## VO 规范（出参，跨服务共享放 gov-common/vo/）

```java
@Data
@Schema(description = "事项信息")
public class ItemVO implements Serializable {
    @Schema(description = "事项ID") private Long id;
    @Schema(description = "事项名称") private String itemName;
    @Schema(description = "状态") private Integer status;
    @Schema(description = "创建时间") private LocalDateTime createTime;
}
```

## 分层边界（硬性约束）

```
Controller → 入 DTO，出 VO
Service    → 入基本类型或 DTO（内部转 Entity），出 Entity
Mapper     → 只操作 Entity
Entity     → 纯数据库映射，禁止逃逸到 Controller 层
```

## 铁律

1. Entity 必须继承 `BaseEntity` + `@EqualsAndHashCode(callSuper = true)`
2. Entity 字段必须加 `@Schema(description = "xxx")`
3. 数据库表名下划线 ← → Entity 驼峰（mybatis-plus 自动映射）
4. 禁止 Controller 入参直接用 Entity → 必须用 DTO
5. 禁止 Controller 返回值直接用 Entity → 必须用 VO
6. 跨服务共享的 VO 放在 `gov-common/vo/`，仅自己用的放本服务 `vo/`
7. DTO 放在各自服务的 `dto/` 包，每个 DTO 提供 `toEntity()` 方法
8. DTO/VO 转换用 `BeanUtil.copyProperties()` 或 MapStruct
9. 禁止使用 `java.util.Date` → 全部用 `LocalDateTime`
10. 状态字段用 `Integer`，注释中写明枚举含义

## 禁止

- ❌ 不继承 BaseEntity
- ❌ 字段不加 @Schema
- ❌ Controller 直接暴露 Entity（见分层边界图）
- ❌ 用 Date / Timestamp
