package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.user.dto.DictDTO;
import com.gov.user.service.DictService;
import com.gov.user.vo.DictVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;

@Tag(name = "数据字典管理", description = "数据字典管理接口")
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
@Validated
public class DictController {

    private final DictService dictService;

    @Operation(summary = "根据类型查询字典列表")
    @GetMapping("/type/{dictType}")
    public Result<List<DictVO>> listByType(
            @Parameter(description = "字典类型") @PathVariable String dictType) {
        return Result.success(dictService.listVOByType(dictType));
    }

    @Operation(summary = "分页查询字典")
    @GetMapping("/page")
    public Result<PageResult<DictVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "字典类型") @RequestParam(required = false) String dictType) {
        return Result.success(dictService.pageQueryVO(pageNum, pageSize, dictType));
    }

    @Operation(summary = "根据ID查询字典")
    @GetMapping("/{id}")
    public Result<DictVO> getById(@Parameter(description = "字典ID") @PathVariable Long id) {
        return Result.success(dictService.getVOById(id));
    }

    @Operation(summary = "新增字典")
    @Log(module = "字典管理", action = "新增字典")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody DictDTO dto) {
        dictService.addDict(dto);
        return Result.success();
    }

    @Operation(summary = "修改字典")
    @Log(module = "字典管理", action = "修改字典")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody DictDTO dto) {
        dictService.updateDict(dto);
        return Result.success();
    }

    @Operation(summary = "删除字典")
    @Log(module = "字典管理", action = "删除字典")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "字典ID") @PathVariable Long id) {
        dictService.deleteDict(id);
        return Result.success();
    }
}
