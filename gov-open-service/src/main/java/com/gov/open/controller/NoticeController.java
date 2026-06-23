package com.gov.open.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.open.entity.NoticeEntity;
import com.gov.open.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "通知公告", description = "通知公告")
@RestController
@RequestMapping("/open")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @Operation(summary = "分页查询")
    @GetMapping("/list")
    public Result<PageResult<NoticeEntity>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Long pageSize) {
        LambdaQueryWrapper<NoticeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeEntity::getDeleted, 0);
        wrapper.orderByDesc(NoticeEntity::getCreateTime);
        Page<NoticeEntity> page = noticeService.page(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<NoticeEntity> getById(@Parameter(description = "ID") @PathVariable Long id) {
        NoticeEntity entity = noticeService.getById(id);
        if (entity == null || entity.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        return Result.success(entity);
    }

    @Operation(summary = "新增")
    @Log(module = "政务公开", action = "新增公告")
    @PostMapping
    public Result<Void> add(@Valid @RequestBody NoticeEntity entity) {
        noticeService.save(entity);
        return Result.success();
    }

    @Operation(summary = "更新")
    @Log(module = "政务公开", action = "更新公告")
    @PutMapping
    public Result<Void> update(@Valid @RequestBody NoticeEntity entity) {
        NoticeEntity exist = noticeService.getById(entity.getId());
        if (exist == null || exist.getDeleted() == 1) {
            return Result.notFound("数据不存在");
        }
        noticeService.updateById(entity);
        return Result.success();
    }

    @Operation(summary = "删除")
    @Log(module = "政务公开", action = "删除公告")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "ID") @PathVariable Long id) {
        NoticeEntity entity = noticeService.getById(id);
        if (entity == null) {
            return Result.notFound("数据不存在");
        }
        noticeService.removeById(id);
        return Result.success();
    }
}
