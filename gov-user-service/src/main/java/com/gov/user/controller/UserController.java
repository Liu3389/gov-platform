package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.common.utils.JwtUtil;
import com.gov.common.vo.UserVO;
import com.gov.user.dto.LoginDTO;
import com.gov.user.dto.RegisterDTO;
import com.gov.user.dto.UserDTO;
import com.gov.user.entity.UserEntity;
import com.gov.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "用户管理", description = "用户登录、注册、信息管理接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    // ==================== 登录注册 ====================

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<com.gov.user.vo.LoginVO> login(
            @Parameter(description = "登录请求") @Valid @RequestBody LoginDTO dto) {
        return Result.success(userService.login(dto));
    }

    @Operation(summary = "用户注册")
    @Log(module = "用户管理", action = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(
            @Parameter(description = "注册请求") @Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @Operation(summary = "获取开发用Token（免登录，可选角色。默认admin，可选user/none来测试权限拦截）")
    @GetMapping("/dev-token")
    public Result<String> devToken(
            @Parameter(description = "角色：admin=管理员(默认) | user=普通用户 | none=无角色")
            @RequestParam(defaultValue = "admin") String role,
            @Parameter(description = "用户名（默认admin）")
            @RequestParam(defaultValue = "admin") String username,
            @Parameter(description = "用户ID（默认1）")
            @RequestParam(defaultValue = "1") Long userId) {
        String roles;
        switch (role) {
            case "user":
                roles = "ROLE_USER";
                break;
            case "none":
                roles = "";
                break;
            default:
                roles = "ROLE_ADMIN";
                break;
        }
        String token = JwtUtil.generateTokenWithRoles(userId, username, roles, null, 86400000L);
        return Result.success(token);
    }

    // ==================== 查询 ====================

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/list")
    public Result<PageResult<UserVO>> list(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "用户名") @RequestParam(required = false) String username) {
        return Result.success(userService.pageQuery(pageNum, pageSize, username));
    }

    @Operation(summary = "根据ID查询用户")
    @GetMapping("/{id}")
    public Result<UserVO> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        return Result.success(userService.getVOById(id));
    }

    @Operation(summary = "根据用户名查询用户")
    @GetMapping("/byUsername")
    public Result<UserVO> getByUsername(@Parameter(description = "用户名") @RequestParam String username) {
        return Result.success(userService.getVOByUsername(username));
    }

    // ==================== 新增修改删除 ====================

    @Operation(summary = "新增用户（管理端）")
    @Log(module = "用户管理", action = "新增用户")
    @RequirePermission("user:add")
    @PostMapping
    public Result<Void> add(@Parameter(description = "用户信息") @Valid @RequestBody UserDTO dto) {
        userService.addUser(dto);
        return Result.success();
    }

    @Operation(summary = "更新用户")
    @Log(module = "用户管理", action = "更新用户")
    @RequirePermission("user:edit")
    @PutMapping
    public Result<Void> update(@Parameter(description = "用户信息") @Valid @RequestBody UserDTO dto) {
        userService.updateUser(dto);
        return Result.success();
    }

    @Operation(summary = "删除用户（逻辑删除）")
    @Log(module = "用户管理", action = "删除用户")
    @RequirePermission("user:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }
}
