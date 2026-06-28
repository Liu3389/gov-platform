package com.gov.open.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.open.dto.NoticeDTO;
import com.gov.open.dto.NoticeQueryDTO;
import com.gov.open.service.NoticeService;
import com.gov.open.vo.NoticeVO;
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
 * 通知公告Controller
 */
@Tag(name = "通知公告管理", description = "通知公告管理接口")
@RestController
@RequestMapping("/open/notice")
@RequiredArgsConstructor
@Validated
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "分页查询公告")
    @GetMapping("/list")
    public Result<PageResult<NoticeVO>> list(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            NoticeQueryDTO queryDTO) {
        return Result.success(noticeService.pageQueryVO(pageNum, pageSize, queryDTO));
    }

    @Operation(summary = "根据ID查询公告")
    @GetMapping("/{id}")
    public Result<NoticeVO> getById(@Parameter(description = "公告ID") @PathVariable Long id) {
        NoticeVO vo = noticeService.getVOById(id);
        if (vo == null) {
            return Result.notFound("公告不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "新增公告")
    @Log(module = "通知公告", action = "新增公告")
    @RequirePermission(value = "open:notice:add")
    @PostMapping
    public Result<Void> add(
            @Parameter(description = "发布部门ID") @RequestHeader(value = "X-Dept-Id", required = false) Long publishDeptId,
            @Valid @RequestBody NoticeDTO dto) {
        if (publishDeptId == null) publishDeptId = 1L;
        noticeService.addNotice(dto, publishDeptId);
        return Result.success();
    }

    @Operation(summary = "修改公告")
    @Log(module = "通知公告", action = "修改公告")
    @RequirePermission(value = "open:notice:edit")
    @PutMapping("/{id}")
    public Result<Void> update(
            @Parameter(description = "公告ID") @PathVariable Long id,
            @Valid @RequestBody NoticeDTO dto) {
        noticeService.updateNotice(dto, id);
        return Result.success();
    }

    @Operation(summary = "发布公告")
    @Log(module = "通知公告", action = "发布公告")
    @RequirePermission(value = "open:notice:publish")
    @PostMapping("/publish/{id}")
    public Result<Void> publish(@Parameter(description = "公告ID") @PathVariable Long id) {
        noticeService.publishNotice(id);
        return Result.success();
    }

    @Operation(summary = "撤回公告")
    @Log(module = "通知公告", action = "撤回公告")
    @RequirePermission(value = "open:notice:withdraw")
    @PostMapping("/withdraw/{id}")
    public Result<Void> withdraw(@Parameter(description = "公告ID") @PathVariable Long id) {
        noticeService.withdrawNotice(id);
        return Result.success();
    }

    @Operation(summary = "删除公告")
    @Log(module = "通知公告", action = "删除公告")
    @RequirePermission(value = "open:notice:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "公告ID") @PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }
}
