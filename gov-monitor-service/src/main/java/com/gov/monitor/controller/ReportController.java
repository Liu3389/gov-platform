package com.gov.monitor.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.Result;
import com.gov.monitor.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "报表导出", description = "效能报表Excel导出")
@RestController
@RequestMapping("/monitor/report")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "导出效能Excel报表")
    @Log(module = "报表导出", action = "导出效能报表")
    @RequirePermission("monitor:report:export")
    @PostMapping("/export")
    public void export(
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate,
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
            HttpServletResponse response) {
        reportService.exportEfficiency(response, startDate, endDate, deptId);
    }
}
