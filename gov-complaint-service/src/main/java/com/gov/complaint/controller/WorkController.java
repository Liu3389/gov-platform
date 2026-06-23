package com.gov.complaint.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.complaint.entity.WorkEntity;
import com.gov.complaint.service.WorkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "投诉工单", description = "投诉工单")
@RestController
@RequestMapping("/complaint")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<WorkEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<WorkEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkEntity::getDeleted, 0);
        wrapper.orderByDesc(WorkEntity::getCreateTime);
        Page<WorkEntity> page = workService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<WorkEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        WorkEntity entity = workService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "投诉建议", action = "新增工单")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody WorkEntity entity) {
        workService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "投诉建议", action = "更新工单")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody WorkEntity entity) {
        WorkEntity exist = workService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        workService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除")
    @Log(module = "投诉建议", action = "删除工单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        WorkEntity entity = workService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        workService.removeById(id);
        return Result.success();
    }
}
