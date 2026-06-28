package com.gov.message.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.message.dto.RecordDTO;
import com.gov.message.service.RecordService;
import com.gov.message.vo.RecordVO;
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
 * 消息记录管理
 */
@Tag(name = "消息记录", description = "消息记录管理接口")
@RestController
@RequestMapping("/message/record")
@RequiredArgsConstructor
@Validated
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "分页查询记录")
    @GetMapping("/list")
    public Result<PageResult<RecordVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "发送渠道", example = "sms") @RequestParam(required = false) String channel,
            @Parameter(description = "发送状态", example = "0") @RequestParam(required = false) String sendStatus) {
        return Result.success(recordService.pageQueryVO(pageNum, pageSize, channel, sendStatus));
    }

    @Operation(summary = "根据ID查询记录")
    @GetMapping("/{id}")
    public Result<RecordVO> getById(@Parameter(description = "记录ID", example = "1") @PathVariable Long id) {
        return Result.success(recordService.getVOById(id));
    }

    @Operation(summary = "新增记录")
    @Log(module = "消息通知", action = "新增记录")
    @RequirePermission(value = "message:record:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody RecordDTO dto) {
        recordService.addRecord(dto);
        return Result.success();
    }

    @Operation(summary = "修改记录")
    @Log(module = "消息通知", action = "修改记录")
    @RequirePermission(value = "message:record:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody RecordDTO dto) {
        recordService.updateRecord(dto);
        return Result.success();
    }

    @Operation(summary = "删除记录")
    @Log(module = "消息通知", action = "删除记录")
    @RequirePermission(value = "message:record:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "记录ID", example = "1") @PathVariable Long id) {
        recordService.deleteRecord(id);
        return Result.success();
    }
}
