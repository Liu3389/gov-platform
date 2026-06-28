package com.gov.message.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.message.dto.ConfigDTO;
import com.gov.message.service.ConfigService;
import com.gov.message.vo.ConfigVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 消息配置管理
 */
@Tag(name = "消息配置", description = "消息配置管理接口")
@RestController
@RequestMapping("/message/config")
@RequiredArgsConstructor
@Validated
public class ConfigController {

    private final ConfigService configService;

    @Operation(summary = "分页查询配置")
    @GetMapping("/list")
    public Result<PageResult<ConfigVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "渠道", example = "sms") @RequestParam(required = false) String channel,
            @Parameter(description = "状态", example = "1") @RequestParam(required = false) String status) {
        return Result.success(configService.pageQueryVO(pageNum, pageSize, channel, status));
    }

    @Operation(summary = "根据ID查询配置")
    @GetMapping("/{id}")
    public Result<ConfigVO> getById(@Parameter(description = "配置ID", example = "1") @PathVariable Long id) {
        return Result.success(configService.getVOById(id));
    }

    @Operation(summary = "新增配置")
    @Log(module = "消息通知", action = "新增配置")
    @RequirePermission(value = "message:config:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ConfigDTO dto) {
        configService.addConfig(dto);
        return Result.success();
    }

    @Operation(summary = "修改配置")
    @Log(module = "消息通知", action = "修改配置")
    @RequirePermission(value = "message:config:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ConfigDTO dto) {
        configService.updateConfig(dto);
        return Result.success();
    }

    @Operation(summary = "删除配置")
    @Log(module = "消息通知", action = "删除配置")
    @RequirePermission(value = "message:config:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "配置ID", example = "1") @PathVariable Long id) {
        configService.deleteConfig(id);
        return Result.success();
    }
}
