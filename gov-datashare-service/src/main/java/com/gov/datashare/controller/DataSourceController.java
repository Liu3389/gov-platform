package com.gov.datashare.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.datashare.dto.DataSourceDTO;
import com.gov.datashare.service.DataSourceService;
import com.gov.datashare.vo.DataSourceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "数据源管理", description = "数据源管理接口")
@RestController
@RequestMapping("/share/datasource")
@RequiredArgsConstructor
@Validated
public class DataSourceController {

    private final DataSourceService dataSourceService;

    @Operation(summary = "分页查询数据源")
    @GetMapping("/list")
    public Result<PageResult<DataSourceVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词", example = "用户") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态", example = "1") @RequestParam(required = false) String status) {
        return Result.success(dataSourceService.pageQueryVO(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "根据ID查询数据源")
    @GetMapping("/{id}")
    public Result<DataSourceVO> getById(@Parameter(description = "数据源ID", example = "1") @PathVariable Long id) {
        return Result.success(dataSourceService.getVOById(id));
    }

    @Operation(summary = "新增数据源")
    @Log(module = "数据共享", action = "新增数据源")
    @RequirePermission(value = "datashare:datasource:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody DataSourceDTO dto) {
        dataSourceService.addDataSource(dto);
        return Result.success();
    }

    @Operation(summary = "修改数据源")
    @Log(module = "数据共享", action = "修改数据源")
    @RequirePermission(value = "datashare:datasource:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody DataSourceDTO dto) {
        dataSourceService.updateDataSource(dto);
        return Result.success();
    }

    @Operation(summary = "删除数据源")
    @Log(module = "数据共享", action = "删除数据源")
    @RequirePermission(value = "datashare:datasource:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "数据源ID", example = "1") @PathVariable Long id) {
        dataSourceService.deleteDataSource(id);
        return Result.success();
    }
}
