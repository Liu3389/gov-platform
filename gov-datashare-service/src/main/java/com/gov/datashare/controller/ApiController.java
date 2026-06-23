package com.gov.datashare.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.datashare.entity.ApiEntity;
import com.gov.datashare.service.ApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "共享接口", description = "共享接口")
@RestController
@RequestMapping("/share")
@RequiredArgsConstructor
public class ApiController {

    private final ApiService apiService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<ApiEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<ApiEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApiEntity::getDeleted, 0);
        wrapper.orderByDesc(ApiEntity::getCreateTime);
        Page<ApiEntity> page = apiService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<ApiEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        ApiEntity entity = apiService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "数据共享", action = "新增接口")
    @PostMapping
    public Result<Void> add(@Parameter(description = "共享接口信息") @Valid @RequestBody ApiEntity entity) {
        apiService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "数据共享", action = "更新接口")
    @PutMapping
    public Result<Void> update(@Parameter(description = "共享接口信息") @Valid @RequestBody ApiEntity entity) {
        ApiEntity exist = apiService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        apiService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除")
    @Log(module = "数据共享", action = "删除接口")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        ApiEntity entity = apiService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        apiService.removeById(id);
        return Result.success();
    }
}
