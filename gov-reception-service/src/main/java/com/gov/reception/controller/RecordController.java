package com.gov.reception.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.reception.entity.RecordEntity;
import com.gov.reception.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "办件管理", description = "办件信息管理接口")
@RestController
@RequestMapping("/reception")
@RequiredArgsConstructor
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<RecordEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<RecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RecordEntity::getDeleted, 0);
        wrapper.orderByDesc(RecordEntity::getCreateTime);
        Page<RecordEntity> page = recordService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<RecordEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        RecordEntity entity = recordService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "统一受理", action = "新增办件")
    @PostMapping
    public Result<Void> add(@Parameter(description = "办件信息") @Valid @RequestBody RecordEntity entity) {
        recordService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "统一受理", action = "更新办件")
    @PutMapping
    public Result<Void> update(@Parameter(description = "办件信息") @Valid @RequestBody RecordEntity entity) {
        RecordEntity exist = recordService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        recordService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除（逻辑删除）")
    @Log(module = "统一受理", action = "删除办件")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        RecordEntity entity = recordService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        recordService.removeById(id);
        return Result.success();
    }
}
