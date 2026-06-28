package com.gov.license.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.license.dto.*;
import com.gov.license.service.LicenseService;
import com.gov.license.vo.LicenseDetailVO;
import com.gov.license.vo.LicenseVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "电子证照管理", description = "证照生成、签章、核验、授权等接口")
@RestController
@RequestMapping("/license")
@RequiredArgsConstructor
@Validated
public class LicenseController {

    private final LicenseService licenseService;

    // ==================== 查询 ====================

    @Operation(summary = "分页查询证照列表")
    @RequirePermission(value = "license:query")
    @GetMapping("/list")
    public Result<PageResult<LicenseVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "证照编号") @RequestParam(required = false) String licenseNo,
            @Parameter(description = "关键词（姓名/目录）") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        return Result.success(licenseService.pageQuery(pageNum, pageSize, licenseNo, keyword, status));
    }

    @Operation(summary = "查询证照详情")
    @RequirePermission(value = "license:query")
    @GetMapping("/{id}")
    public Result<LicenseDetailVO> getById(@Parameter(description = "证照ID", required = true) @PathVariable Long id) {
        return Result.success(licenseService.getDetailById(id));
    }

    // ==================== 写操作 ====================

    @Operation(summary = "生成证照（审批办结后调用，内部Feign接口）")
    @Log(module = "电子证照", action = "生成证照")
    @PostMapping
    public Result<LicenseVO> generate(@Valid @RequestBody LicenseGenerateDTO dto) {
        return Result.success(licenseService.generate(dto));
    }

    @Operation(summary = "电子签章")
    @Log(module = "电子证照", action = "签章")
    @RequirePermission(value = "license:sign")
    @PostMapping("/{id}/sign")
    public Result<Void> sign(
            @Parameter(description = "证照ID", required = true) @PathVariable Long id,
            @Parameter(description = "签章人ID（从Token自动获取）", required = true) @RequestParam Long signUserId,
            @Valid @RequestBody LicenseSignDTO dto) {
        dto.setLicenseId(id);
        licenseService.sign(dto, signUserId);
        return Result.success();
    }

    @Operation(summary = "核验证照真伪")
    @Log(module = "电子证照", action = "核验证照")
    @PostMapping("/{id}/verify")
    public Result<LicenseDetailVO> verify(
            @Parameter(description = "证照ID", required = true) @PathVariable Long id,
            @Valid @RequestBody LicenseVerifyDTO dto,
            HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return Result.success(licenseService.verify(dto, ip));
    }

    @Operation(summary = "扫码核验（对外公开）")
    @Log(module = "电子证照", action = "扫码核验")
    @PostMapping("/verify-qr")
    public Result<LicenseDetailVO> verifyQr(
            @Valid @RequestBody LicenseVerifyDTO dto,
            HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return Result.success(licenseService.verify(dto, ip));
    }

    @Operation(summary = "授权他人查看证照")
    @Log(module = "电子证照", action = "授权")
    @RequirePermission(value = "license:auth")
    @PostMapping("/{id}/auth")
    public Result<Void> auth(
            @Parameter(description = "证照ID", required = true) @PathVariable Long id,
            @Parameter(description = "授权人ID（从Token自动获取）", required = true) @RequestParam Long authUserId,
            @Valid @RequestBody LicenseAuthDTO dto) {
        dto.setLicenseId(id);
        licenseService.auth(dto, authUserId);
        return Result.success();
    }

    @Operation(summary = "更新证照信息")
    @Log(module = "电子证照", action = "更新证照")
    @RequirePermission(value = "license:edit")
    @PutMapping("/{id}")
    public Result<Void> update(
            @Parameter(description = "证照ID", required = true) @PathVariable Long id,
            @Valid @RequestBody LicenseGenerateDTO dto) {
        licenseService.updateLicense(id, dto);
        return Result.success();
    }

    // ==================== 管理操作 ====================

    @Operation(summary = "删除证照（逻辑删除）")
    @Log(module = "电子证照", action = "删除证照")
    @RequirePermission(value = "license:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "证照ID", required = true) @PathVariable Long id) {
        licenseService.deleteLicense(id);
        return Result.success();
    }
}
