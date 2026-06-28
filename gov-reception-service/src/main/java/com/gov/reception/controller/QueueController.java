package com.gov.reception.controller;

import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.reception.service.QueueService;
import com.gov.reception.vo.QueueVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 排队叫号Controller
 */
@Tag(name = "排队叫号", description = "排队叫号管理接口")
@RestController
@RequestMapping("/reception/queue")
@RequiredArgsConstructor
@Validated
public class QueueController {

    private final QueueService queueService;

    @Operation(summary = "分页查询排队")
    @GetMapping("/list")
    public Result<PageResult<QueueVO>> list(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "窗口ID") @RequestParam(required = false) Long windowId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        return Result.success(queueService.pageQueryVO(pageNum, pageSize, windowId, status));
    }

    @Operation(summary = "取号排队")
    @Log(module = "排队叫号", action = "取号排队")
    @PostMapping("/take")
    public Result<QueueVO> takeQueue(
            @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @Parameter(description = "事项ID") @RequestParam Long itemId) {
        if (userId == null) userId = 1L;
        return Result.success(queueService.takeQueue(userId, itemId));
    }

    @Operation(summary = "叫号")
    @Log(module = "排队叫号", action = "叫号")
    @PostMapping("/call/{id}")
    public Result<Void> call(
            @Parameter(description = "排队ID") @PathVariable Long id,
            @Parameter(description = "窗口ID") @RequestParam Long windowId) {
        queueService.callQueue(id, windowId);
        return Result.success();
    }

    @Operation(summary = "完成办理")
    @Log(module = "排队叫号", action = "完成办理")
    @PostMapping("/finish/{id}")
    public Result<Void> finish(@Parameter(description = "排队ID") @PathVariable Long id) {
        queueService.finishQueue(id);
        return Result.success();
    }

    @Operation(summary = "取消排队")
    @Log(module = "排队叫号", action = "取消排队")
    @PostMapping("/cancel/{id}")
    public Result<Void> cancel(@Parameter(description = "排队ID") @PathVariable Long id) {
        queueService.cancelQueue(id);
        return Result.success();
    }
}
