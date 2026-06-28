package com.gov.monitor.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.event.OperateLogEvent;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.monitor.dto.LogDTO;
import com.gov.monitor.service.LogService;
import com.gov.monitor.vo.LogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Tag(name = "操作日志", description = "操作日志查询与管理")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
@Validated
public class LogController {

    private final LogService logService;

    @Operation(summary = "分页查询操作日志")
    @GetMapping("/list")
    public Result<PageResult<LogVO>> list(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize) {
        return Result.success(logService.pageQueryVO(pageNum, pageSize));
    }

    @Operation(summary = "审计日志查询（按操作时间筛选）")
    @GetMapping("/audit")
    public Result<PageResult<LogVO>> audit(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "操作开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime operateStart,
            @Parameter(description = "操作结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime operateEnd) {
        return Result.success(logService.pageAuditVO(pageNum, pageSize, operateStart, operateEnd));
    }

    @Operation(summary = "根据ID查询操作日志")
    @GetMapping("/{id}")
    public Result<LogVO> getById(@Parameter(description = "ID") @PathVariable Long id) {
        return Result.success(logService.getVOById(id));
    }

    @Operation(summary = "新增操作日志（管理端手动录入）")
    @Log(module = "操作日志", action = "新增日志")
    @RequirePermission("monitor:add")
    @PostMapping
    public Result<Void> add(@Parameter(description = "日志信息") @Valid @RequestBody LogDTO dto) {
        logService.addLog(dto);
        return Result.success();
    }

    @Operation(summary = "删除操作日志")
    @Log(module = "操作日志", action = "删除日志")
    @RequirePermission("monitor:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        logService.deleteLog(id);
        return Result.success();
    }

    // ==================== 内部接口：接收 AOP 操作日志落库 ====================

    /**
     * 操作日志落库接口（内部调用，由 gov-common 的 OperateLogEventListener 通过 Feign 调用）
     *
     * <p>此接口不对外暴露，不添加 @Operation 生成 Knife4j 文档，
     * 仅用于服务间内部通信。</p>
     */
    @PostMapping("/log/record")
    public Result<Void> record(@Parameter(description = "操作日志事件") @RequestBody OperateLogEvent event) {
        logService.recordLog(event);
        return Result.success();
    }
}
