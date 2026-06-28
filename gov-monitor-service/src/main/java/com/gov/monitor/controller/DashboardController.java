package com.gov.monitor.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.Result;
import com.gov.monitor.dto.DashboardStatDTO;
import com.gov.monitor.service.DashboardService;
import com.gov.monitor.vo.DashboardOverviewVO;
import com.gov.monitor.vo.DashboardTrendVO;
import com.gov.monitor.vo.DeptRankVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "数据大屏", description = "数据大屏概览、趋势与排名")
@RestController
@RequestMapping("/monitor/dashboard")
@RequiredArgsConstructor
@Validated
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "数据大屏概览")
    @GetMapping("/overview")
    public Result<DashboardOverviewVO> overview() {
        return Result.success(dashboardService.getOverview());
    }

    @Operation(summary = "近N天趋势")
    @GetMapping("/trend")
    public Result<List<DashboardTrendVO>> trend(
            @Parameter(description = "天数") @RequestParam(defaultValue = "30") Integer days) {
        return Result.success(dashboardService.getTrend(days));
    }

    @Operation(summary = "部门效率排名")
    @GetMapping("/dept-rank")
    public Result<List<DeptRankVO>> deptRank() {
        return Result.success(dashboardService.getDeptRank());
    }

    @Operation(summary = "手动录入统计数据")
    @Log(module = "数据大屏", action = "录入统计")
    @RequirePermission("monitor:dashboard:add")
    @PostMapping("/stat")
    public Result<Void> addStat(@Valid @RequestBody DashboardStatDTO dto) {
        dashboardService.addStat(dto);
        return Result.success();
    }
}
