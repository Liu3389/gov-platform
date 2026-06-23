package com.gov.item.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.item.entity.ItemEntity;
import com.gov.item.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "事项管理", description = "事项信息管理接口")
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<ItemEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<ItemEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ItemEntity::getDeleted, 0);
        wrapper.orderByDesc(ItemEntity::getCreateTime);
        Page<ItemEntity> page = itemService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<ItemEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        ItemEntity entity = itemService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "事项管理", action = "新增事项")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ItemEntity entity) {
        itemService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "事项管理", action = "更新事项")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ItemEntity entity) {
        ItemEntity exist = itemService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        itemService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除（逻辑删除）")
    @Log(module = "事项管理", action = "删除事项")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        ItemEntity entity = itemService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        itemService.removeById(id);
        return Result.success();
    }
}
