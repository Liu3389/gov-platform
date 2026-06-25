package com.gov.complaint.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.complaint.dto.SuggestionDTO;
import com.gov.complaint.service.SuggestionService;
import com.gov.complaint.vo.SuggestionVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@Tag(name = "建议征集", description = "建议征集管理接口")
@RestController
@RequestMapping("/complaint/suggestion")
@RequiredArgsConstructor
@Validated
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Operation(summary = "分页查询建议")
    @GetMapping("/list")
    public Result<PageResult<SuggestionVO>> list(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词", example = "公交") @RequestParam(required = false) String keyword,
            @Parameter(description = "状态", example = "0") @RequestParam(required = false) String status) {
        return Result.success(suggestionService.pageQueryVO(pageNum, pageSize, keyword, status));
    }

    @Operation(summary = "根据ID查询建议")
    @GetMapping("/{id}")
    public Result<SuggestionVO> getById(@Parameter(description = "建议ID", example = "1") @PathVariable Long id) {
        return Result.success(suggestionService.getVOById(id));
    }

    @Operation(summary = "新增建议")
    @Log(module = "投诉建议", action = "新增建议")
    @RequirePermission(value = "complaint:suggestion:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody SuggestionDTO dto) {
        suggestionService.addSuggestion(dto);
        return Result.success();
    }

    @Operation(summary = "修改建议")
    @Log(module = "投诉建议", action = "修改建议")
    @RequirePermission(value = "complaint:suggestion:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody SuggestionDTO dto) {
        suggestionService.updateSuggestion(dto);
        return Result.success();
    }

    @Operation(summary = "回复建议")
    @Log(module = "投诉建议", action = "回复建议")
    @RequirePermission(value = "complaint:suggestion:reply")
    @PutMapping("/{id}/reply")
    public Result<Void> reply(
            @Parameter(description = "建议ID", example = "1") @PathVariable Long id,
            @Parameter(description = "回复内容", example = "感谢您的建议，已纳入下季度规划。") @RequestParam String replyContent) {
        suggestionService.replySuggestion(id, replyContent);
        return Result.success();
    }

    @Operation(summary = "删除建议")
    @Log(module = "投诉建议", action = "删除建议")
    @RequirePermission(value = "complaint:suggestion:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "建议ID", example = "1") @PathVariable Long id) {
        suggestionService.deleteSuggestion(id);
        return Result.success();
    }
}
