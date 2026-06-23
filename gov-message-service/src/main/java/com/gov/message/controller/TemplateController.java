package com.gov.message.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.message.entity.TemplateEntity;
import com.gov.message.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "消息模板", description = "消息模板")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<TemplateEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<TemplateEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TemplateEntity::getDeleted, 0);
        wrapper.orderByDesc(TemplateEntity::getCreateTime);
        Page<TemplateEntity> page = templateService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<TemplateEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        TemplateEntity entity = templateService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "消息通知", action = "新增模板")
    @PostMapping
    public Result<Void> add(@Parameter(description = "模板信息") @Valid @RequestBody TemplateEntity entity) {
        templateService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "消息通知", action = "更新模板")
    @PutMapping
    public Result<Void> update(@Parameter(description = "模板信息") @Valid @RequestBody TemplateEntity entity) {
        TemplateEntity exist = templateService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        templateService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除")
    @Log(module = "消息通知", action = "删除模板")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        TemplateEntity entity = templateService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        templateService.removeById(id);
        return Result.success();
    }
}
