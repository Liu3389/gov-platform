package com.gov.complaint.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.complaint.dto.CategoryDTO;
import com.gov.complaint.service.CategoryService;
import com.gov.complaint.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "投诉分类", description = "投诉分类管理接口")
@RestController
@RequestMapping("/complaint/category")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "分页查询分类")
    @GetMapping("/list")
    public Result<PageResult<CategoryVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词", example = "市政") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态", example = "1") @RequestParam(required = false) String status) {
        return Result.success(categoryService.pageQueryVO(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "根据ID查询分类")
    @GetMapping("/{id}")
    public Result<CategoryVO> getById(@Parameter(description = "分类ID", example = "1") @PathVariable Long id) {
        return Result.success(categoryService.getVOById(id));
    }

    @Operation(summary = "新增分类")
    @Log(module = "投诉建议", action = "新增分类")
    @RequirePermission(value = "complaint:category:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody CategoryDTO dto) {
        categoryService.addCategory(dto);
        return Result.success();
    }

    @Operation(summary = "修改分类")
    @Log(module = "投诉建议", action = "修改分类")
    @RequirePermission(value = "complaint:category:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody CategoryDTO dto) {
        categoryService.updateCategory(dto);
        return Result.success();
    }

    @Operation(summary = "删除分类")
    @Log(module = "投诉建议", action = "删除分类")
    @RequirePermission(value = "complaint:category:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "分类ID", example = "1") @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
