package com.gov.message.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.message.dto.InboxDTO;
import com.gov.message.service.InboxService;
import com.gov.message.vo.InboxVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

/**
 * 站内信管理
 */
@Tag(name = "站内信", description = "站内信管理接口")
@RestController
@RequestMapping("/message/inbox")
@RequiredArgsConstructor
@Validated
public class InboxController {

    private final InboxService inboxService;

    @Operation(summary = "分页查询站内信")
    @GetMapping("/list")
    public Result<PageResult<InboxVO>> list(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小", example = "10") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "用户ID", example = "100") @RequestParam(required = false) Long userId,
            @Parameter(description = "是否已读", example = "0") @RequestParam(required = false) Integer isRead) {
        return Result.success(inboxService.pageQueryVO(pageNum, pageSize, userId, isRead));
    }

    @Operation(summary = "根据ID查询站内信")
    @GetMapping("/{id}")
    public Result<InboxVO> getById(@Parameter(description = "站内信ID", example = "1") @PathVariable Long id) {
        return Result.success(inboxService.getVOById(id));
    }

    @Operation(summary = "新增站内信")
    @Log(module = "消息通知", action = "新增站内信")
    @RequirePermission(value = "message:inbox:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody InboxDTO dto) {
        inboxService.addInbox(dto);
        return Result.success();
    }

    @Operation(summary = "修改站内信")
    @Log(module = "消息通知", action = "修改站内信")
    @RequirePermission(value = "message:inbox:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody InboxDTO dto) {
        inboxService.updateInbox(dto);
        return Result.success();
    }

    @Operation(summary = "删除站内信")
    @Log(module = "消息通知", action = "删除站内信")
    @RequirePermission(value = "message:inbox:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "站内信ID", example = "1") @PathVariable Long id) {
        inboxService.deleteInbox(id);
        return Result.success();
    }

    @Operation(summary = "标记为已读")
    @Log(module = "消息通知", action = "标记站内信已读")
    @RequirePermission(value = "message:inbox:read")
    @PutMapping("/{id}/read")
    public Result<Void> markAsRead(@Parameter(description = "站内信ID", example = "1") @PathVariable Long id) {
        inboxService.markAsRead(id);
        return Result.success();
    }
}
