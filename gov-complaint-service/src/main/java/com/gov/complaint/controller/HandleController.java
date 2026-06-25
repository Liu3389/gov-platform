package com.gov.complaint.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.complaint.dto.HandleDTO;
import com.gov.complaint.service.HandleService;
import com.gov.complaint.vo.HandleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;

@Tag(name = "处理反馈", description = "工单处理反馈接口")
@RestController
@RequestMapping("/complaint/handle")
@RequiredArgsConstructor
@Validated
public class HandleController {

    private final HandleService handleService;

    @Operation(summary = "分页查询处理记录")
    @GetMapping("/list")
    public Result<PageResult<HandleVO>> list(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "工单ID", example = "1") @RequestParam(required = false) Long workId) {
        return Result.success(handleService.pageQueryVO(pageNum, pageSize, workId));
    }

    @Operation(summary = "根据工单ID查询处理记录")
    @GetMapping("/work/{workId}")
    public Result<List<HandleVO>> listByWorkId(@Parameter(description = "工单ID", example = "1") @PathVariable Long workId) {
        return Result.success(handleService.listByWorkId(workId));
    }

    @Operation(summary = "根据ID查询处理记录")
    @GetMapping("/{id}")
    public Result<HandleVO> getById(@Parameter(description = "处理记录ID", example = "1") @PathVariable Long id) {
        return Result.success(handleService.getVOById(id));
    }

    @Operation(summary = "新增处理记录")
    @Log(module = "投诉建议", action = "新增处理")
    @RequirePermission(value = "complaint:handle:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody HandleDTO dto) {
        handleService.addHandle(dto);
        return Result.success();
    }

    @Operation(summary = "修改处理记录")
    @Log(module = "投诉建议", action = "修改处理")
    @RequirePermission(value = "complaint:handle:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody HandleDTO dto) {
        handleService.updateHandle(dto);
        return Result.success();
    }

    @Operation(summary = "删除处理记录")
    @Log(module = "投诉建议", action = "删除处理")
    @RequirePermission(value = "complaint:handle:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "处理记录ID", example = "1") @PathVariable Long id) {
        handleService.deleteHandle(id);
        return Result.success();
    }
}
