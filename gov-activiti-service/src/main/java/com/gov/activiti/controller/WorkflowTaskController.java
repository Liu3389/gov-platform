package com.gov.activiti.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.activiti.entity.WorkflowTaskEntity;
import com.gov.activiti.service.WorkflowTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "工作流任务管理", description = "工作流任务管理接口")
@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
public class WorkflowTaskController {

    private final WorkflowTaskService workflowTaskService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<WorkflowTaskEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<WorkflowTaskEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkflowTaskEntity::getDeleted, 0);
        wrapper.orderByDesc(WorkflowTaskEntity::getCreateTime);
        Page<WorkflowTaskEntity> page = workflowTaskService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<WorkflowTaskEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        WorkflowTaskEntity entity = workflowTaskService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "工作流管理", action = "新增任务")
    @PostMapping
    public Result<Void> add(@Parameter(description = "工作流任务信息") @Valid @RequestBody WorkflowTaskEntity entity) {
        workflowTaskService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "工作流管理", action = "更新任务")
    @PutMapping
    public Result<Void> update(@Parameter(description = "工作流任务信息") @Valid @RequestBody WorkflowTaskEntity entity) {
        WorkflowTaskEntity exist = workflowTaskService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        workflowTaskService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除（逻辑删除）")
    @Log(module = "工作流管理", action = "删除任务")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        WorkflowTaskEntity entity = workflowTaskService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        workflowTaskService.removeById(id);
        return Result.success();
    }
}
