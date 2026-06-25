package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.Result;
import com.gov.user.dto.MenuDTO;
import com.gov.user.service.MenuService;
import com.gov.user.vo.MenuVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "菜单管理", description = "菜单信息管理接口")
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @Operation(summary = "查询所有菜单")
    @GetMapping("/list")
    public Result<List<MenuVO>> list() {
        return Result.success(menuService.listAllVO());
    }

    @Operation(summary = "根据ID查询菜单")
    @GetMapping("/{id}")
    public Result<MenuVO> getById(@Parameter(description = "菜单ID") @PathVariable Long id) {
        return Result.success(menuService.getVOById(id));
    }

    @Operation(summary = "查询用户的菜单列表")
    @GetMapping("/user/{userId}")
    public Result<List<MenuVO>> listByUserId(@Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(menuService.listMenusVOByUserId(userId));
    }

    @Operation(summary = "查询角色的菜单列表")
    @GetMapping("/role/{roleId}")
    public Result<List<MenuVO>> listByRoleId(@Parameter(description = "角色ID") @PathVariable Long roleId) {
        return Result.success(menuService.listMenusVOByRoleId(roleId));
    }

    @Operation(summary = "新增菜单")
    @Log(module = "菜单管理", action = "新增菜单")
    @RequirePermission("menu:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody MenuDTO dto) {
        menuService.addMenu(dto);
        return Result.success();
    }

    @Operation(summary = "修改菜单")
    @Log(module = "菜单管理", action = "修改菜单")
    @RequirePermission("menu:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody MenuDTO dto) {
        menuService.updateMenu(dto);
        return Result.success();
    }

    @Operation(summary = "删除菜单")
    @Log(module = "菜单管理", action = "删除菜单")
    @RequirePermission("menu:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "菜单ID") @PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success();
    }
}
