package com.gov.reception.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.Result;
import com.gov.reception.service.MaterialService;
import com.gov.reception.vo.MaterialVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 材料管理Controller
 */
@Tag(name = "材料管理", description = "申报材料管理接口")
@RestController
@RequestMapping("/reception/material")
@RequiredArgsConstructor
@Validated
public class MaterialController {

    private final MaterialService materialService;

    @Operation(summary = "查询办件材料列表")
    @GetMapping("/list/{recordId}")
    public Result<List<MaterialVO>> listByRecordId(
            @Parameter(description = "办件ID") @PathVariable Long recordId) {
        return Result.success(materialService.listByRecordId(recordId));
    }

    @Operation(summary = "审核材料")
    @Log(module = "统一受理", action = "审核材料")
    @RequirePermission(value = "reception:verify")
    @PostMapping("/verify")
    public Result<Void> verify(
            @Parameter(description = "材料ID") @RequestParam Long id,
            @Parameter(description = "审核状态：1合格 2不合格 3缺失") @RequestParam Integer verifyStatus,
            @Parameter(description = "审核备注") @RequestParam(required = false) String verifyRemark,
            @Parameter(description = "审核人ID") @RequestHeader(value = "X-User-Id", required = false) Long verifyBy) {
        if (verifyBy == null) verifyBy = 1L;
        materialService.verifyMaterial(id, verifyStatus, verifyRemark, verifyBy);
        return Result.success();
    }
}
