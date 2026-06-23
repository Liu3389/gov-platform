package com.gov.monitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.event.OperateLogEvent;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.monitor.entity.LogEntity;
import com.gov.monitor.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "操作日志", description = "操作日志查询与管理")
@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class LogController {

    private final LogService logService;

    @Operation(summary = "分页查询操作日志")
    @GetMapping("/list")
    public Result<PageResult<LogEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<LogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LogEntity::getDeleted, 0);
        wrapper.orderByDesc(LogEntity::getOperateTime);
        Page<LogEntity> page = logService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询操作日志")
    @GetMapping("/{id}")
    public Result<LogEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        LogEntity entity = logService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增操作日志（管理端手动录入）")
    @Log(module = "操作日志", action = "新增日志")
    @PostMapping
    public Result<Void> add(@Parameter(description = "日志信息") @Valid @RequestBody LogEntity entity) {
        logService.save(entity);
        return Result.success();
    }

    @Operation(summary = "删除操作日志")
    @Log(module = "操作日志", action = "删除日志")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        LogEntity entity = logService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        logService.removeById(id);
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
        LogEntity entity = new LogEntity();
        entity.setUserId(event.getUserId());
        entity.setUserName(event.getUserName());
        entity.setDeptId(event.getDeptId());
        entity.setDeptName(event.getDeptName());
        entity.setModule(event.getModule());
        entity.setAction(event.getAction());
        entity.setMethod(event.getMethod());
        entity.setRequestUrl(event.getRequestUrl());
        entity.setRequestType(event.getRequestType());
        entity.setRequestParams(event.getRequestParams());
        entity.setResponseData(event.getResponseData());
        entity.setOperateTime(event.getOperateTime());
        entity.setOperateIp(event.getOperateIp());
        // executeTime 从 Long 转 Integer
        entity.setExecuteTime(event.getExecuteTime() != null ? event.getExecuteTime().intValue() : null);
        entity.setStatus(event.getStatus());
        entity.setErrorMsg(event.getErrorMsg());

        logService.save(entity);
        return Result.success();
    }
}
