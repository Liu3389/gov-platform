package com.gov.open.controller;

import com.gov.common.annotation.Log;
import com.gov.common.result.Result;
import com.gov.open.dto.CatalogDTO;
import com.gov.open.service.CatalogService;
import com.gov.open.vo.CatalogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 公开目录Controller
 */
@Tag(name = "公开目录管理", description = "公开目录管理接口")
@RestController
@RequestMapping("/open/catalog")
@RequiredArgsConstructor
@Validated
public class CatalogController {

    private final CatalogService catalogService;

    @Operation(summary = "查询目录树")
    @GetMapping("/tree")
    public Result<List<CatalogVO>> tree(
            @Parameter(description = "目录类型") @RequestParam(required = false) Integer catalogType) {
        return Result.success(catalogService.getCatalogTree(catalogType));
    }

    @Operation(summary = "根据ID查询目录")
    @GetMapping("/{id}")
    public Result<CatalogVO> getById(@Parameter(description = "目录ID") @PathVariable Long id) {
        CatalogVO vo = catalogService.getVOById(id);
        if (vo == null) {
            return Result.notFound("目录不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "新增目录")
    @Log(module = "公开目录", action = "新增目录")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody CatalogDTO dto) {
        catalogService.addCatalog(dto);
        return Result.success();
    }

    @Operation(summary = "修改目录")
    @Log(module = "公开目录", action = "修改目录")
    @PutMapping("/{id}")
    public Result<Void> update(
            @Parameter(description = "目录ID") @PathVariable Long id,
            @Valid @RequestBody CatalogDTO dto) {
        catalogService.updateCatalog(dto, id);
        return Result.success();
    }

    @Operation(summary = "删除目录")
    @Log(module = "公开目录", action = "删除目录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "目录ID") @PathVariable Long id) {
        catalogService.deleteCatalog(id);
        return Result.success();
    }
}
