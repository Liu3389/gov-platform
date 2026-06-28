package com.gov.datashare.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.datashare.dto.ApiDTO;
import com.gov.datashare.service.ApiService;
import com.gov.datashare.vo.ApiVO;
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
 * 共享接口管理
 */
@Tag(name = "共享接口", description = "共享接口管理")
@RestController
@RequestMapping("/share/api")
@RequiredArgsConstructor
@Validated
public class ApiController {

    private final ApiService apiService;

    @Operation(summary = "分页查询接口")
    @GetMapping("/list")
    public Result<PageResult<ApiVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词", example = "数据") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态", example = "1") @RequestParam(required = false) String status) {
        return Result.success(apiService.pageQueryVO(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "根据ID查询接口")
    @GetMapping("/{id}")
    public Result<ApiVO> getById(@Parameter(description = "接口ID") @PathVariable Long id) {
        return Result.success(apiService.getVOById(id));
    }

    @Operation(summary = "新增接口")
    @Log(module = "数据共享", action = "新增接口")
    @RequirePermission(value = "datashare:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody ApiDTO dto) {
        apiService.addApi(dto);
        return Result.success();
    }

    @Operation(summary = "修改接口")
    @Log(module = "数据共享", action = "修改接口")
    @RequirePermission(value = "datashare:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody ApiDTO dto) {
        apiService.updateApi(dto);
        return Result.success();
    }

    @Operation(summary = "删除接口")
    @Log(module = "数据共享", action = "删除接口")
    @RequirePermission(value = "datashare:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "接口ID") @PathVariable Long id) {
        apiService.deleteApi(id);
        return Result.success();
    }
}
