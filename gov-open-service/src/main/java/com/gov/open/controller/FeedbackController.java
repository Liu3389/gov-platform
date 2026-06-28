package com.gov.open.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.open.dto.FeedbackDTO;
import com.gov.open.service.FeedbackService;
import com.gov.open.vo.FeedbackVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

/**
 * 公开反馈Controller
 */
@Tag(name = "公开反馈管理", description = "公开反馈管理接口")
@RestController
@RequestMapping("/open/feedback")
@RequiredArgsConstructor
@Validated
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "分页查询反馈")
    @GetMapping("/list")
    public Result<PageResult<FeedbackVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "内容类型") @RequestParam(required = false) Integer contentType,
            @Parameter(description = "内容ID") @RequestParam(required = false) Long contentId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        return Result.success(feedbackService.pageQueryVO(pageNum, pageSize, contentType, contentId, status));
    }

    @Operation(summary = "根据ID查询反馈")
    @GetMapping("/{id}")
    public Result<FeedbackVO> getById(@Parameter(description = "反馈ID") @PathVariable Long id) {
        FeedbackVO vo = feedbackService.getVOById(id);
        if (vo == null) {
            return Result.notFound("反馈不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "提交反馈")
    @Log(module = "公开反馈", action = "提交反馈")
    @PostMapping("/submit")
    public Result<Void> submit(
            @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @Valid @RequestBody FeedbackDTO dto) {
        if (userId == null) userId = 1L;
        feedbackService.submitFeedback(dto, userId);
        return Result.success();
    }

    @Operation(summary = "回复反馈")
    @Log(module = "公开反馈", action = "回复反馈")
    @RequirePermission(value = "open:feedback:reply")
    @PostMapping("/reply/{id}")
    public Result<Void> reply(
            @Parameter(description = "反馈ID") @PathVariable Long id,
            @Parameter(description = "回复内容") @RequestParam String replyContent,
            @Parameter(description = "回复人ID") @RequestHeader(value = "X-User-Id", required = false) Long replyBy) {
        if (replyBy == null) replyBy = 1L;
        feedbackService.replyFeedback(id, replyContent, replyBy);
        return Result.success();
    }

    @Operation(summary = "关闭反馈")
    @Log(module = "公开反馈", action = "关闭反馈")
    @RequirePermission(value = "open:feedback:close")
    @PostMapping("/close/{id}")
    public Result<Void> close(@Parameter(description = "反馈ID") @PathVariable Long id) {
        feedbackService.closeFeedback(id);
        return Result.success();
    }
}
