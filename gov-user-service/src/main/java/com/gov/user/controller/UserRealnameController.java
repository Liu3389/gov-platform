package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.user.dto.UserRealnameDTO;
import com.gov.user.service.UserRealnameService;
import com.gov.user.vo.UserRealnameVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@Tag(name = "实名认证管理", description = "实名认证管理接口")
@RestController
@RequestMapping("/realname")
@RequiredArgsConstructor
@Validated
public class UserRealnameController {

    private final UserRealnameService userRealnameService;

    @Operation(summary = "查询用户实名认证信息")
    @GetMapping("/user/{userId}")
    public Result<UserRealnameVO> getByUserId(
            @Parameter(description = "用户ID") @PathVariable Long userId) {
        return Result.success(userRealnameService.getVOByUserId(userId));
    }

    @Operation(summary = "分页查询实名认证记录")
    @GetMapping("/page")
    public Result<PageResult<UserRealnameVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "审核状态：0待审 1通过 2驳回") @RequestParam(required = false) Integer verifyStatus) {
        return Result.success(userRealnameService.pageQueryVO(pageNum, pageSize, verifyStatus));
    }

    @Operation(summary = "提交实名认证申请")
    @Log(module = "实名认证", action = "提交认证")
    @RequirePermission("realname:submit")
    @PostMapping("/submit")
    public Result<Void> submit(@Valid @RequestBody UserRealnameDTO dto) {
        userRealnameService.submitRealname(dto);
        return Result.success();
    }

    @Operation(summary = "审核实名认证")
    @Log(module = "实名认证", action = "审核认证")
    @PutMapping("/verify/{id}")
    public Result<Void> verify(
            @Parameter(description = "认证ID") @PathVariable Long id,
            @Parameter(description = "审核状态：1通过 2驳回") @RequestParam Integer verifyStatus,
            @Parameter(description = "审核备注") @RequestParam(required = false) String verifyRemark) {
        userRealnameService.verifyRealname(id, verifyStatus, verifyRemark);
        return Result.success();
    }
}
