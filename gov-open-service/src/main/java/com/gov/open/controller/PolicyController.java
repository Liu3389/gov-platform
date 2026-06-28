package com.gov.open.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.open.dto.PolicyDTO;
import com.gov.open.service.PolicyService;
import com.gov.open.vo.PolicyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

/**
 * 政策法规Controller
 */
@Tag(name = "政策法规管理", description = "政策法规管理接口")
@RestController
@RequestMapping("/open/policy")
@RequiredArgsConstructor
@Validated
public class PolicyController {

    private final PolicyService policyService;

    @Operation(summary = "分页查询政策法规")
    @GetMapping("/list")
    public Result<PageResult<PolicyVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "政策类型") @RequestParam(required = false) Integer policyType,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        return Result.success(policyService.pageQueryVO(pageNum, pageSize, policyType, status));
    }

    @Operation(summary = "根据ID查询政策法规")
    @GetMapping("/{id}")
    public Result<PolicyVO> getById(@Parameter(description = "政策ID") @PathVariable Long id) {
        PolicyVO vo = policyService.getVOById(id);
        if (vo == null) {
            return Result.notFound("政策法规不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "新增政策法规")
    @Log(module = "政策法规", action = "新增政策法规")
    @RequirePermission(value = "open:policy:add")
    @PostMapping
    public Result<Void> add(
            @Parameter(description = "发布部门ID") @RequestHeader(value = "X-Dept-Id", required = false) Long publishDeptId,
            @Valid @RequestBody PolicyDTO dto) {
        if (publishDeptId == null) publishDeptId = 1L;
        policyService.addPolicy(dto, publishDeptId);
        return Result.success();
    }

    @Operation(summary = "修改政策法规")
    @Log(module = "政策法规", action = "修改政策法规")
    @RequirePermission(value = "open:policy:edit")
    @PutMapping("/{id}")
    public Result<Void> update(
            @Parameter(description = "政策ID") @PathVariable Long id,
            @Valid @RequestBody PolicyDTO dto) {
        policyService.updatePolicy(dto, id);
        return Result.success();
    }

    @Operation(summary = "发布政策法规")
    @Log(module = "政策法规", action = "发布政策法规")
    @RequirePermission(value = "open:policy:publish")
    @PostMapping("/publish/{id}")
    public Result<Void> publish(@Parameter(description = "政策ID") @PathVariable Long id) {
        policyService.publishPolicy(id);
        return Result.success();
    }

    @Operation(summary = "废止政策法规")
    @Log(module = "政策法规", action = "废止政策法规")
    @RequirePermission(value = "open:policy:abolish")
    @PostMapping("/abolish/{id}")
    public Result<Void> abolish(@Parameter(description = "政策ID") @PathVariable Long id) {
        policyService.abolishPolicy(id);
        return Result.success();
    }

    @Operation(summary = "删除政策法规")
    @Log(module = "政策法规", action = "删除政策法规")
    @RequirePermission(value = "open:policy:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "政策ID") @PathVariable Long id) {
        policyService.removeById(id);
        return Result.success();
    }
}
