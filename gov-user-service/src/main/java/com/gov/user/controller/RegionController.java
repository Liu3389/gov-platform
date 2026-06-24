package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.result.Result;
import com.gov.user.dto.RegionDTO;
import com.gov.user.service.RegionService;
import com.gov.user.vo.RegionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "行政区划管理", description = "行政区划管理接口")
@RestController
@RequestMapping("/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @Operation(summary = "查询所有行政区划")
    @GetMapping("/list")
    public Result<List<RegionVO>> list() {
        return Result.success(regionService.listAllVO());
    }

    @Operation(summary = "根据父级编码查询子区划")
    @GetMapping("/children")
    public Result<List<RegionVO>> listByParent(
            @Parameter(description = "父级区划编码") @RequestParam String parentCode) {
        return Result.success(regionService.listVOByParentCode(parentCode));
    }

    @Operation(summary = "根据ID查询区划")
    @GetMapping("/{id}")
    public Result<RegionVO> getById(@Parameter(description = "区划ID") @PathVariable Long id) {
        return Result.success(regionService.getVOById(id));
    }

    @Operation(summary = "根据区划编码查询")
    @GetMapping("/byCode")
    public Result<RegionVO> getByCode(
            @Parameter(description = "区划编码") @RequestParam String regionCode) {
        return Result.success(regionService.getVOByCode(regionCode));
    }

    @Operation(summary = "新增区划")
    @Log(module = "区划管理", action = "新增区划")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody RegionDTO dto) {
        regionService.addRegion(dto);
        return Result.success();
    }

    @Operation(summary = "修改区划")
    @Log(module = "区划管理", action = "修改区划")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody RegionDTO dto) {
        regionService.updateRegion(dto);
        return Result.success();
    }

    @Operation(summary = "删除区划")
    @Log(module = "区划管理", action = "删除区划")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "区划ID") @PathVariable Long id) {
        regionService.deleteRegion(id);
        return Result.success();
    }
}
