package com.gov.complaint.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.complaint.dto.WorkDTO;
import com.gov.complaint.service.WorkService;
import com.gov.complaint.vo.WorkVO;
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
 * 投诉工单管理
 */
@Tag(name = "投诉工单", description = "投诉工单管理接口")
@RestController
@RequestMapping("/complaint/work")
@RequiredArgsConstructor
@Validated
public class WorkController {

    private final WorkService workService;

    @Operation(summary = "分页查询工单")
    @GetMapping("/list")
    public Result<PageResult<WorkVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词", example = "路灯") @RequestParam(required = false) String keyword,
            @Parameter(description = "工单状态", example = "0") @RequestParam(required = false) String status) {
        return Result.success(workService.pageQueryVO(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "根据ID查询工单")
    @GetMapping("/{id}")
    public Result<WorkVO> getById(@Parameter(description = "工单ID", example = "1") @PathVariable Long id) {
        return Result.success(workService.getVOById(id));
    }

    @Operation(summary = "新增工单")
    @Log(module = "投诉建议", action = "新增工单")
    @RequirePermission(value = "complaint:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody WorkDTO dto) {
        workService.addWork(dto);
        return Result.success();
    }

    @Operation(summary = "修改工单")
    @Log(module = "投诉建议", action = "修改工单")
    @RequirePermission(value = "complaint:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody WorkDTO dto) {
        workService.updateWork(dto);
        return Result.success();
    }

    @Operation(summary = "删除工单")
    @Log(module = "投诉建议", action = "删除工单")
    @RequirePermission(value = "complaint:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "工单ID", example = "1") @PathVariable Long id) {
        workService.deleteWork(id);
        return Result.success();
    }
}
