package com.gov.complaint.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.complaint.dto.HotspotDTO;
import com.gov.complaint.service.HotspotService;
import com.gov.complaint.vo.HotspotVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Tag(name = "热点分析", description = "投诉热点分析接口")
@RestController
@RequestMapping("/complaint/hotspot")
@RequiredArgsConstructor
@Validated
public class HotspotController {

    private final HotspotService hotspotService;

    @Operation(summary = "分页查询热点")
    @GetMapping("/list")
    public Result<PageResult<HotspotVO>> list(
            @Parameter(description = "页码", example = "1") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词", example = "路灯") @RequestParam(required = false) String keyword) {
        return Result.success(hotspotService.pageQueryVO(pageNum, pageSize, keyword));
    }

    @Operation(summary = "根据ID查询热点")
    @GetMapping("/{id}")
    public Result<HotspotVO> getById(@Parameter(description = "热点ID", example = "1") @PathVariable Long id) {
        return Result.success(hotspotService.getVOById(id));
    }

    @Operation(summary = "新增热点")
    @Log(module = "投诉建议", action = "新增热点")
    @RequirePermission(value = "complaint:hotspot:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody HotspotDTO dto) {
        hotspotService.addHotspot(dto);
        return Result.success();
    }

    @Operation(summary = "修改热点")
    @Log(module = "投诉建议", action = "修改热点")
    @RequirePermission(value = "complaint:hotspot:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody HotspotDTO dto) {
        hotspotService.updateHotspot(dto);
        return Result.success();
    }

    @Operation(summary = "删除热点")
    @Log(module = "投诉建议", action = "删除热点")
    @RequirePermission(value = "complaint:hotspot:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "热点ID", example = "1") @PathVariable Long id) {
        hotspotService.deleteHotspot(id);
        return Result.success();
    }
}
