package com.gov.license.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.license.entity.LicenseEntity;
import com.gov.license.service.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "证照数据", description = "证照数据")
@RestController
@RequestMapping("/license")
@RequiredArgsConstructor
public class LicenseController {

    private final LicenseService licenseService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<LicenseEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<LicenseEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LicenseEntity::getDeleted, 0);
        wrapper.orderByDesc(LicenseEntity::getCreateTime);
        Page<LicenseEntity> page = licenseService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<LicenseEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        LicenseEntity entity = licenseService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "电子证照", action = "新增证照")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody LicenseEntity entity) {
        licenseService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "电子证照", action = "更新证照")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody LicenseEntity entity) {
        LicenseEntity exist = licenseService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        licenseService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除")
    @Log(module = "电子证照", action = "删除证照")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        LicenseEntity entity = licenseService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        licenseService.removeById(id);
        return Result.success();
    }
}
