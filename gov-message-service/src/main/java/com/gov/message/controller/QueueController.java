package com.gov.message.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.message.dto.QueueDTO;
import com.gov.message.service.QueueService;
import com.gov.message.vo.QueueVO;
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
 * 消息队列管理
 */
@Tag(name = "消息队列", description = "消息队列管理接口")
@RestController
@RequestMapping("/message/queue")
@RequiredArgsConstructor
@Validated
public class QueueController {

    private final QueueService queueService;

    @Operation(summary = "分页查询队列")
    @GetMapping("/list")
    public Result<PageResult<QueueVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "队列状态", example = "0") @RequestParam(required = false) Integer queueStatus,
            @Parameter(description = "优先级", example = "0") @RequestParam(required = false) Integer priority) {
        return Result.success(queueService.pageQueryVO(pageNum, pageSize, queueStatus, priority));
    }

    @Operation(summary = "根据ID查询队列")
    @GetMapping("/{id}")
    public Result<QueueVO> getById(@Parameter(description = "队列ID", example = "1") @PathVariable Long id) {
        return Result.success(queueService.getVOById(id));
    }

    @Operation(summary = "新增队列")
    @Log(module = "消息通知", action = "新增队列")
    @RequirePermission(value = "message:queue:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody QueueDTO dto) {
        queueService.addQueue(dto);
        return Result.success();
    }

    @Operation(summary = "修改队列")
    @Log(module = "消息通知", action = "修改队列")
    @RequirePermission(value = "message:queue:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody QueueDTO dto) {
        queueService.updateQueue(dto);
        return Result.success();
    }

    @Operation(summary = "删除队列")
    @Log(module = "消息通知", action = "删除队列")
    @RequirePermission(value = "message:queue:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "队列ID", example = "1") @PathVariable Long id) {
        queueService.deleteQueue(id);
        return Result.success();
    }
}
