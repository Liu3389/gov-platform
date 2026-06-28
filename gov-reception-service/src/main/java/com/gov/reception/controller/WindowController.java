package com.gov.reception.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.reception.dto.WindowDTO;
import com.gov.reception.service.WindowService;
import com.gov.reception.vo.WindowVO;
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
 * 窗口管理Controller
 */
@Tag(name = "窗口管理", description = "窗口信息管理接口")
@RestController
@RequestMapping("/reception/window")
@RequiredArgsConstructor
@Validated
public class WindowController {

    private final WindowService windowService;

    @Operation(summary = "分页查询窗口")
    @GetMapping("/list")
    public Result<PageResult<WindowVO>> list(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        return Result.success(windowService.pageQueryVO(pageNum, pageSize, deptId, status));
    }

    @Operation(summary = "根据ID查询窗口")
    @GetMapping("/{id}")
    public Result<WindowVO> getById(@Parameter(description = "窗口ID") @PathVariable Long id) {
        WindowVO vo = windowService.getVOById(id);
        if (vo == null) {
            return Result.notFound("窗口不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "新增窗口")
    @Log(module = "窗口管理", action = "新增窗口")
    @RequirePermission(value = "reception:window:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody WindowDTO dto) {
        windowService.addWindow(dto);
        return Result.success();
    }

    @Operation(summary = "修改窗口")
    @Log(module = "窗口管理", action = "修改窗口")
    @RequirePermission(value = "reception:window:edit")
    @PutMapping("/{id}")
    public Result<Void> update(
            @Parameter(description = "窗口ID") @PathVariable Long id,
            @Valid @RequestBody WindowDTO dto) {
        windowService.updateWindow(dto, id);
        return Result.success();
    }

    @Operation(summary = "删除窗口")
    @Log(module = "窗口管理", action = "删除窗口")
    @RequirePermission(value = "reception:window:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "窗口ID") @PathVariable Long id) {
        windowService.removeById(id);
        return Result.success();
    }
}
