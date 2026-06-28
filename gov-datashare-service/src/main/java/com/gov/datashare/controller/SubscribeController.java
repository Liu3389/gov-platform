package com.gov.datashare.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.datashare.dto.SubscribeDTO;
import com.gov.datashare.service.SubscribeService;
import com.gov.datashare.vo.SubscribeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "订阅管理", description = "接口订阅管理接口")
@RestController
@RequestMapping("/share/subscribe")
@RequiredArgsConstructor
@Validated
public class SubscribeController {

    private final SubscribeService subscribeService;

    @Operation(summary = "分页查询订阅")
    @GetMapping("/list")
    public Result<PageResult<SubscribeVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "接口ID", example = "1") @RequestParam(required = false) Long apiId,
            @Parameter(description = "状态", example = "1") @RequestParam(required = false) String status) {
        return Result.success(subscribeService.pageQueryVO(pageNum, pageSize, apiId, status));
    }

    @Operation(summary = "根据ID查询订阅")
    @GetMapping("/{id}")
    public Result<SubscribeVO> getById(@Parameter(description = "订阅ID", example = "1") @PathVariable Long id) {
        return Result.success(subscribeService.getVOById(id));
    }

    @Operation(summary = "新增订阅")
    @Log(module = "数据共享", action = "新增订阅")
    @RequirePermission(value = "datashare:subscribe:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody SubscribeDTO dto) {
        subscribeService.addSubscribe(dto);
        return Result.success();
    }

    @Operation(summary = "修改订阅")
    @Log(module = "数据共享", action = "修改订阅")
    @RequirePermission(value = "datashare:subscribe:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody SubscribeDTO dto) {
        subscribeService.updateSubscribe(dto);
        return Result.success();
    }

    @Operation(summary = "删除订阅")
    @Log(module = "数据共享", action = "删除订阅")
    @RequirePermission(value = "datashare:subscribe:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "订阅ID", example = "1") @PathVariable Long id) {
        subscribeService.deleteSubscribe(id);
        return Result.success();
    }
}
