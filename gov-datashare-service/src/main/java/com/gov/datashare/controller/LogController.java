package com.gov.datashare.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.datashare.dto.LogDTO;
import com.gov.datashare.service.LogService;
import com.gov.datashare.vo.LogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@Tag(name = "交换日志", description = "交换日志管理接口")
@RestController
@RequestMapping("/share/log")
@RequiredArgsConstructor
@Validated
public class LogController {

    private final LogService logService;

    @Operation(summary = "分页查询日志")
    @GetMapping("/list")
    public Result<PageResult<LogVO>> list(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "接口ID", example = "1") @RequestParam(required = false) Long apiId,
            @Parameter(description = "调用结果", example = "success") @RequestParam(required = false) String callResult) {
        return Result.success(logService.pageQueryVO(pageNum, pageSize, apiId, callResult));
    }

    @Operation(summary = "根据ID查询日志")
    @GetMapping("/{id}")
    public Result<LogVO> getById(@Parameter(description = "日志ID", example = "1") @PathVariable Long id) {
        return Result.success(logService.getVOById(id));
    }

    @Operation(summary = "新增日志")
    @Log(module = "数据共享", action = "新增日志")
    @RequirePermission(value = "datashare:log:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody LogDTO dto) {
        logService.addLog(dto);
        return Result.success();
    }

    @Operation(summary = "修改日志")
    @Log(module = "数据共享", action = "修改日志")
    @RequirePermission(value = "datashare:log:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody LogDTO dto) {
        logService.updateLog(dto);
        return Result.success();
    }

    @Operation(summary = "删除日志")
    @Log(module = "数据共享", action = "删除日志")
    @RequirePermission(value = "datashare:log:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "日志ID", example = "1") @PathVariable Long id) {
        logService.deleteLog(id);
        return Result.success();
    }
}
