package com.gov.monitor.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.Result;
import com.gov.monitor.dto.EfficiencyStatDTO;
import com.gov.monitor.dto.SatisfactionStatDTO;
import com.gov.monitor.service.StatService;
import com.gov.monitor.vo.EfficiencyStatVO;
import com.gov.monitor.vo.SatisfactionStatVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "效能统计", description = "效能统计与满意度趋势查询")
@RestController
@RequestMapping("/monitor/stat")
@RequiredArgsConstructor
@Validated
public class StatController {

    private final StatService statService;

    @Operation(summary = "效能统计查询")
    @GetMapping("/efficiency")
    public Result<List<EfficiencyStatVO>> efficiency(
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        return Result.success(statService.getEfficiency(deptId, startDate, endDate));
    }

    @Operation(summary = "满意度趋势查询")
    @GetMapping("/satisfaction")
    public Result<List<SatisfactionStatVO>> satisfaction(
            @Parameter(description = "部门ID") @RequestParam(required = false) Long deptId,
            @Parameter(description = "开始日期") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) String endDate) {
        return Result.success(statService.getSatisfaction(deptId, startDate, endDate));
    }

    @Operation(summary = "手动录入效能统计")
    @Log(module = "效能统计", action = "录入效能统计")
    @RequirePermission("monitor:stat:add")
    @PostMapping("/efficiency")
    public Result<Void> addEfficiency(@Valid @RequestBody EfficiencyStatDTO dto) {
        statService.addEfficiencyStat(dto);
        return Result.success();
    }

    @Operation(summary = "手动录入满意度统计")
    @Log(module = "效能统计", action = "录入满意度统计")
    @RequirePermission("monitor:stat:add")
    @PostMapping("/satisfaction")
    public Result<Void> addSatisfaction(@Valid @RequestBody SatisfactionStatDTO dto) {
        statService.addSatisfactionStat(dto);
        return Result.success();
    }
}
