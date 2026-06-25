package com.gov.message.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.message.dto.TemplateDTO;
import com.gov.message.service.TemplateService;
import com.gov.message.vo.TemplateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

/**
 * 消息模板管理
 */
@Tag(name = "消息模板", description = "消息模板管理接口")
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Validated
public class TemplateController {

    private final TemplateService templateService;

    @Operation(summary = "分页查询模板")
    @GetMapping("/list")
    public Result<PageResult<TemplateVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "发送渠道") @RequestParam(required = false) String channel,
            @Parameter(description = "状态") @RequestParam(required = false) String status) {
        return Result.success(templateService.pageQueryVO(pageNum, pageSize, keyword, channel, status));
    }

    @Operation(summary = "根据ID查询模板")
    @GetMapping("/{id}")
    public Result<TemplateVO> getById(@Parameter(description = "模板ID") @PathVariable Long id) {
        return Result.success(templateService.getVOById(id));
    }

    @Operation(summary = "新增模板")
    @Log(module = "消息通知", action = "新增模板")
    @RequirePermission(value = "message:add")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody TemplateDTO dto) {
        templateService.addTemplate(dto);
        return Result.success();
    }

    @Operation(summary = "修改模板")
    @Log(module = "消息通知", action = "修改模板")
    @RequirePermission(value = "message:edit")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody TemplateDTO dto) {
        templateService.updateTemplate(dto);
        return Result.success();
    }

    @Operation(summary = "删除模板")
    @Log(module = "消息通知", action = "删除模板")
    @RequirePermission(value = "message:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "模板ID") @PathVariable Long id) {
        templateService.deleteTemplate(id);
        return Result.success();
    }
}
