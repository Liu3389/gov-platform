package com.gov.message.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.message.dto.MessageSendDTO;
import com.gov.message.service.MessageService;
import com.gov.message.utils.UserContext;
import com.gov.message.vo.MessageSendVO;
import com.gov.message.vo.RecordVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

/**
 * 消息发送管理
 */
@Tag(name = "消息发送", description = "消息发送管理接口")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Validated
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "发送消息")
    @Log(module = "消息通知", action = "发送消息")
    @RequirePermission("message:send")
    @PostMapping("/send")
    public Result<MessageSendVO> sendMessage(@Valid @RequestBody MessageSendDTO dto) {
        return Result.success(messageService.sendMessage(dto));
    }

    @Operation(summary = "查询未读消息数")
    @GetMapping("/unread")
    public Result<Long> countUnread(
            @Parameter(description = "用户ID", example = "100") @RequestParam(required = false) Long userId) {
        // 如果未传入userId，则使用当前登录用户ID
        if (userId == null) {
            userId = UserContext.getUserId();
        }
        return Result.success(messageService.countUnread(userId));
    }
}
