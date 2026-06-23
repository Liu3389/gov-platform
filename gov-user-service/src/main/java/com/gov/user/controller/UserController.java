package com.gov.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.user.entity.UserEntity;
import com.gov.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器（示例模板）
 * 其他组员参考此模板编写 Controller
 */
@Tag(name = "用户管理", description = "用户登录、注册、信息管理接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "分页查询用户列表")
    @GetMapping("/list")
    public Result<PageResult<UserEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize,
            @Parameter(description = "用户名") @RequestParam(required = false) String username) {

        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getDeleted, 0);
        if (username != null && !username.isEmpty()) {
            wrapper.like(UserEntity::getUsername, username);
        }
        wrapper.orderByDesc(UserEntity::getCreateTime);

        Page<UserEntity> page = userService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询用户")
    @GetMapping("/{id}")
    public Result<UserEntity> getById(@Parameter(description = "用户ID") @PathVariable Long id) {
        UserEntity user = userService.getById(id);
        if (user == null || user.getDeleted() == 1) {
            return Result.notFound("用户不存在");
        }
        return Result.success(user);
    }

    @Operation(summary = "新增用户")
    @Log(module = "用户管理", action = "新增用户")
    @PostMapping
    public Result<Void> add(@Parameter(description = "用户信息") @Valid @RequestBody UserEntity user) {
        // 检查用户名是否已存在
        if (userService.getByUsername(user.getUsername()) != null) {
            return Result.fail("用户名已存在");
        }
        userService.save(user);
        return Result.success();
    }

    @Operation(summary = "更新用户")
    @Log(module = "用户管理", action = "更新用户")
    @PutMapping
    public Result<Void> update(@Parameter(description = "用户信息") @Valid @RequestBody UserEntity user) {
        UserEntity exist = userService.getById(user.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("用户不存在");
        }
        userService.updateById(user);
        return Result.success();
    }

    @Operation(summary = "删除用户（逻辑删除）")
    @Log(module = "用户管理", action = "删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "用户ID") @PathVariable Long id) {
        UserEntity user = userService.getById(id);
        if (user == null) {
            return Result.notFound("用户不存在");
        }
        userService.removeById(id); // MyBatis-Plus 逻辑删除
        return Result.success();
    }
}