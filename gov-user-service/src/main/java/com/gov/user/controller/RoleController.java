package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.user.dto.RoleDTO;
import com.gov.user.service.RoleService;
import com.gov.user.vo.RoleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Tag(name = "角色管理", description = "角色信息管理接口")
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Validated
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "查询所有角色")
    @GetMapping("/list")
    public Result<List<RoleVO>> list() {
        return Result.success(roleService.listAllVO());
    }

    @Operation(summary = "分页查询角色")
    @GetMapping("/page")
    public Result<PageResult<RoleVO>> page(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "角色名称") @RequestParam(required = false) String roleName) {
        return Result.success(roleService.pageQueryVO(pageNum, pageSize, roleName));
    }

    @Operation(summary = "根据ID查询角色")
    @GetMapping("/{id}")
    public Result<RoleVO> getById(@Parameter(description = "角色ID") @PathVariable Long id) {
        return Result.success(roleService.getVOById(id));
    }

    @Operation(summary = "根据角色编码查询")
    @GetMapping("/byCode")
    public Result<RoleVO> getByCode(@Parameter(description = "角色编码") @RequestParam String roleCode) {
        return Result.success(roleService.getVOByRoleCode(roleCode));
    }

    @Operation(summary = "查询用户的角色列表")
    @GetMapping("/user/{userId}")
    public Result<List<RoleVO>> listByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(roleService.listRolesVOByUserId(userId));
    }

    @Operation(summary = "新增角色")
    @Log(module = "角色管理", action = "新增角色")
    @RequirePermission("role:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody RoleDTO dto) {
        roleService.addRole(dto);
        return Result.success();
    }

    @Operation(summary = "修改角色")
    @Log(module = "角色管理", action = "修改角色")
    @RequirePermission("role:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody RoleDTO dto) {
        roleService.updateRole(dto);
        return Result.success();
    }

    @Operation(summary = "删除角色")
    @Log(module = "角色管理", action = "删除角色")
    @RequirePermission("role:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "角色ID") @PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }
}
