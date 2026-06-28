package com.gov.datashare.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.datashare.dto.CatalogDTO;
import com.gov.datashare.service.CatalogService;
import com.gov.datashare.vo.CatalogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "共享目录", description = "共享目录管理接口")
@RestController
@RequestMapping("/share/catalog")
@RequiredArgsConstructor
@Validated
public class CatalogController {

    private final CatalogService catalogService;

    @Operation(summary = "分页查询目录")
    @GetMapping("/list")
    public Result<PageResult<CatalogVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词", example = "用户") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态", example = "1") @RequestParam(required = false) String status) {
        return Result.success(catalogService.pageQueryVO(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "根据ID查询目录")
    @GetMapping("/{id}")
    public Result<CatalogVO> getById(@Parameter(description = "目录ID", example = "1") @PathVariable Long id) {
        return Result.success(catalogService.getVOById(id));
    }

    @Operation(summary = "新增目录")
    @Log(module = "数据共享", action = "新增目录")
    @RequirePermission(value = "datashare:catalog:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody CatalogDTO dto) {
        catalogService.addCatalog(dto);
        return Result.success();
    }

    @Operation(summary = "修改目录")
    @Log(module = "数据共享", action = "修改目录")
    @RequirePermission(value = "datashare:catalog:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody CatalogDTO dto) {
        catalogService.updateCatalog(dto);
        return Result.success();
    }

    @Operation(summary = "删除目录")
    @Log(module = "数据共享", action = "删除目录")
    @RequirePermission(value = "datashare:catalog:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "目录ID", example = "1") @PathVariable Long id) {
        catalogService.deleteCatalog(id);
        return Result.success();
    }
}
