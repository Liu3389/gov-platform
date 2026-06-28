package com.gov.complaint.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.complaint.dto.SuperviseDTO;
import com.gov.complaint.service.SuperviseService;
import com.gov.complaint.vo.SuperviseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Tag(name = "督办管理", description = "工单督办管理接口")
@RestController
@RequestMapping("/complaint/supervise")
@RequiredArgsConstructor
@Validated
public class SuperviseController {

    private final SuperviseService superviseService;

    @Operation(summary = "分页查询督办记录")
    @GetMapping("/list")
    public Result<PageResult<SuperviseVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "工单ID", example = "1") @RequestParam(required = false) Long workId,
            @Parameter(description = "状态", example = "0") @RequestParam(required = false) String status) {
        return Result.success(superviseService.pageQueryVO(pageNum, pageSize, workId, status));
    }

    @Operation(summary = "根据工单ID查询督办记录")
    @GetMapping("/work/{workId}")
    public Result<List<SuperviseVO>> listByWorkId(@Parameter(description = "工单ID", example = "1") @PathVariable Long workId) {
        return Result.success(superviseService.listByWorkId(workId));
    }

    @Operation(summary = "根据ID查询督办记录")
    @GetMapping("/{id}")
    public Result<SuperviseVO> getById(@Parameter(description = "督办记录ID", example = "1") @PathVariable Long id) {
        return Result.success(superviseService.getVOById(id));
    }

    @Operation(summary = "新增督办记录")
    @Log(module = "投诉建议", action = "新增督办")
    @RequirePermission(value = "complaint:supervise:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody SuperviseDTO dto) {
        superviseService.addSupervise(dto);
        return Result.success();
    }

    @Operation(summary = "修改督办记录")
    @Log(module = "投诉建议", action = "修改督办")
    @RequirePermission(value = "complaint:supervise:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody SuperviseDTO dto) {
        superviseService.updateSupervise(dto);
        return Result.success();
    }

    @Operation(summary = "删除督办记录")
    @Log(module = "投诉建议", action = "删除督办")
    @RequirePermission(value = "complaint:supervise:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "督办记录ID", example = "1") @PathVariable Long id) {
        superviseService.deleteSupervise(id);
        return Result.success();
    }
}
