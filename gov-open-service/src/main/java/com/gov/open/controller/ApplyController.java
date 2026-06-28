package com.gov.open.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.open.dto.ApplyDTO;
import com.gov.open.service.ApplyService;
import com.gov.open.vo.ApplyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 依申请公开Controller
 */
@Tag(name = "依申请公开管理", description = "依申请公开管理接口")
@RestController
@RequestMapping("/open/apply")
@RequiredArgsConstructor
@Validated
public class ApplyController {

    private final ApplyService applyService;

    @Operation(summary = "分页查询依申请公开")
    @GetMapping("/list")
    public Result<PageResult<ApplyVO>> list(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        return Result.success(applyService.pageQueryVO(pageNum, pageSize, status));
    }

    @Operation(summary = "根据ID查询依申请公开")
    @GetMapping("/{id}")
    public Result<ApplyVO> getById(@Parameter(description = "申请ID") @PathVariable Long id) {
        ApplyVO vo = applyService.getVOById(id);
        if (vo == null) {
            return Result.notFound("申请不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "提交申请")
    @Log(module = "依申请公开", action = "提交申请")
    @PostMapping("/submit")
    public Result<ApplyVO> submit(
            @Parameter(description = "用户ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @Parameter(description = "用户姓名") @RequestHeader(value = "X-Username", required = false) String userName,
            @Valid @RequestBody ApplyDTO dto) {
        if (userId == null) userId = 1L;
        if (userName == null) userName = "匿名用户";
        ApplyVO vo = applyService.submitApply(dto, userId, userName, null, null);
        return Result.success(vo);
    }

    @Operation(summary = "受理申请")
    @Log(module = "依申请公开", action = "受理申请")
    @RequirePermission(value = "open:apply:accept")
    @PostMapping("/accept/{id}")
    public Result<Void> accept(@Parameter(description = "申请ID") @PathVariable Long id) {
        applyService.acceptApply(id);
        return Result.success();
    }

    @Operation(summary = "答复申请")
    @Log(module = "依申请公开", action = "答复申请")
    @RequirePermission(value = "open:apply:reply")
    @PostMapping("/reply/{id}")
    public Result<Void> reply(
            @Parameter(description = "申请ID") @PathVariable Long id,
            @Parameter(description = "答复内容") @RequestParam String replyContent,
            @Parameter(description = "答复人ID") @RequestHeader(value = "X-User-Id", required = false) Long replyBy) {
        if (replyBy == null) replyBy = 1L;
        applyService.replyApply(id, replyContent, replyBy);
        return Result.success();
    }

    @Operation(summary = "不予公开")
    @Log(module = "依申请公开", action = "不予公开")
    @RequirePermission(value = "open:apply:reject")
    @PostMapping("/reject/{id}")
    public Result<Void> reject(
            @Parameter(description = "申请ID") @PathVariable Long id,
            @Parameter(description = "不予公开原因") @RequestParam String rejectReason) {
        applyService.rejectApply(id, rejectReason);
        return Result.success();
    }
}
