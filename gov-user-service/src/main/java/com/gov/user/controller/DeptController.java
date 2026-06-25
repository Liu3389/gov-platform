package com.gov.user.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.user.dto.DeptDTO;
import com.gov.user.entity.DeptEntity;
import com.gov.user.service.DeptService;
import com.gov.user.vo.DeptVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import java.util.List;

@Tag(name = "部门管理", description = "部门信息管理接口")
@RestController
@RequestMapping("/dept")
@RequiredArgsConstructor
@Validated
public class DeptController {

    private final DeptService deptService;

    @Operation(summary = "查询所有部门")
    @GetMapping("/list")
    public Result<List<DeptVO>> list() {
        return Result.success(deptService.listAllVO());
    }

    @Operation(summary = "分页查询部门")
    @GetMapping("/page")
    public Result<PageResult<DeptVO>> page(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "部门名称") @RequestParam(required = false) String deptName) {
        return Result.success(deptService.pageQueryVO(pageNum, pageSize, deptName));
    }

    @Operation(summary = "根据ID查询部门")
    @GetMapping("/{id}")
    public Result<DeptVO> getById(@Parameter(description = "部门ID") @PathVariable Long id) {
        return Result.success(deptService.getVOById(id));
    }

    @Operation(summary = "根据部门编码查询")
    @GetMapping("/byCode")
    public Result<DeptVO> getByCode(@Parameter(description = "部门编码") @RequestParam String deptCode) {
        return Result.success(deptService.getVOByDeptCode(deptCode));
    }

    @Operation(summary = "新增部门")
    @Log(module = "部门管理", action = "新增部门")
    @RequirePermission("dept:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody DeptDTO dto) {
        deptService.addDept(dto);
        return Result.success();
    }

    @Operation(summary = "修改部门")
    @Log(module = "部门管理", action = "修改部门")
    @RequirePermission("dept:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody DeptDTO dto) {
        deptService.updateDept(dto);
        return Result.success();
    }

    @Operation(summary = "删除部门")
    @Log(module = "部门管理", action = "删除部门")
    @RequirePermission("dept:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "部门ID") @PathVariable Long id) {
        deptService.deleteDept(id);
        return Result.success();
    }
}
