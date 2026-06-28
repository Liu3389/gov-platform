package com.gov.datashare.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.datashare.dto.PermissionDTO;
import com.gov.datashare.service.PermissionService;
import com.gov.datashare.vo.PermissionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@Tag(name = "接口权限", description = "接口权限管理接口")
@RestController
@RequestMapping("/share/permission")
@RequiredArgsConstructor
@Validated
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "分页查询权限")
    @GetMapping("/list")
    public Result<PageResult<PermissionVO>> list(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "接口ID", example = "1") @RequestParam(required = false) Long apiId,
            @Parameter(description = "状态", example = "1") @RequestParam(required = false) String status) {
        return Result.success(permissionService.pageQueryVO(pageNum, pageSize, apiId, status));
    }

    @Operation(summary = "根据ID查询权限")
    @GetMapping("/{id}")
    public Result<PermissionVO> getById(@Parameter(description = "权限ID", example = "1") @PathVariable Long id) {
        return Result.success(permissionService.getVOById(id));
    }

    @Operation(summary = "新增权限")
    @Log(module = "数据共享", action = "新增权限")
    @RequirePermission(value = "datashare:permission:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody PermissionDTO dto) {
        permissionService.addPermission(dto);
        return Result.success();
    }

    @Operation(summary = "修改权限")
    @Log(module = "数据共享", action = "修改权限")
    @RequirePermission(value = "datashare:permission:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody PermissionDTO dto) {
        permissionService.updatePermission(dto);
        return Result.success();
    }

    @Operation(summary = "删除权限")
    @Log(module = "数据共享", action = "删除权限")
    @RequirePermission(value = "datashare:permission:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "权限ID", example = "1") @PathVariable Long id) {
        permissionService.deletePermission(id);
        return Result.success();
    }
}
