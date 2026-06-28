package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.user.dto.LoginLogDTO;
import com.gov.user.service.LoginLogService;
import com.gov.user.vo.LoginLogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "登录日志管理", description = "登录日志管理接口")
@RestController
@RequestMapping("/login-log")
@RequiredArgsConstructor
@Validated
public class LoginLogController {

    private final LoginLogService loginLogService;

    @Operation(summary = "分页查询登录日志")
    @GetMapping("/page")
    public Result<PageResult<LoginLogVO>> page(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "用户ID") @RequestParam(required = false) Long userId) {
        return Result.success(loginLogService.pageQueryVO(pageNum, pageSize, userId));
    }

    @Operation(summary = "根据ID查询登录日志")
    @GetMapping("/{id}")
    public Result<LoginLogVO> getById(@Parameter(description = "日志ID") @PathVariable Long id) {
        return Result.success(loginLogService.getVOById(id));
    }

    @Operation(summary = "记录登录日志")
    @Log(module = "登录日志", action = "记录登录")
    @RequirePermission("log:record")
    @PostMapping("/record")
    public Result<Void> record(@Valid @RequestBody LoginLogDTO dto) {
        loginLogService.recordLogin(dto);
        return Result.success();
    }
}
