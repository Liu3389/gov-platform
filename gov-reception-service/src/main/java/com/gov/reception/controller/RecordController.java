package com.gov.reception.controller;

import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.reception.dto.RecordAcceptDTO;
import com.gov.reception.dto.RecordQueryDTO;
import com.gov.reception.dto.RecordSubmitDTO;
import com.gov.reception.service.RecordService;
import com.gov.reception.vo.RecordProgressVO;
import com.gov.reception.vo.RecordVO;
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
 * 办件管理Controller
 */
@Tag(name = "办件管理", description = "办件信息管理接口")
@RestController
@RequestMapping("/reception")
@RequiredArgsConstructor
@Validated
public class RecordController {

    private final RecordService recordService;

    @Operation(summary = "提交办件")
    @Log(module = "统一受理", action = "提交办件")
    @PostMapping("/submit")
    public Result<RecordVO> submit(
            @Parameter(description = "申请人ID") @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestBody RecordSubmitDTO dto) {
        // 如果没有用户ID，默认使用1（测试用）
        if (userId == null) {
            userId = 1L;
        }
        return Result.success(recordService.submitRecord(dto, userId));
    }

    @Operation(summary = "受理登记")
    @Log(module = "统一受理", action = "受理登记")
    @RequirePermission(value = "reception:accept")
    @PostMapping("/accept")
    public Result<Void> accept(
            @Parameter(description = "操作人ID") @RequestHeader(value = "X-User-Id", required = false) Long operatorId,
            @Parameter(description = "操作人姓名") @RequestHeader(value = "X-Username", required = false) String operatorName,
            @Valid @RequestBody RecordAcceptDTO dto) {
        if (operatorId == null) operatorId = 1L;
        if (operatorName == null) operatorName = "admin";
        recordService.acceptRecord(dto, operatorId, operatorName);
        return Result.success();
    }

    @Operation(summary = "分页查询办件")
    @GetMapping("/list")
    public Result<PageResult<RecordVO>> list(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            RecordQueryDTO queryDTO) {
        return Result.success(recordService.pageQueryVO(pageNum, pageSize, queryDTO));
    }

    @Operation(summary = "根据ID查询办件")
    @GetMapping("/{id}")
    public Result<RecordVO> getById(@Parameter(description = "办件ID") @PathVariable Long id) {
        RecordVO vo = recordService.getVOById(id);
        if (vo == null) {
            return Result.notFound("办件不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "查询办件进度")
    @GetMapping("/progress/{id}")
    public Result<RecordProgressVO> getProgress(@Parameter(description = "办件ID") @PathVariable Long id) {
        RecordProgressVO vo = recordService.getRecordProgress(id);
        if (vo == null) {
            return Result.notFound("办件不存在");
        }
        return Result.success(vo);
    }

    @Operation(summary = "删除办件")
    @Log(module = "统一受理", action = "删除办件")
    @RequirePermission(value = "reception:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "办件ID") @PathVariable Long id) {
        recordService.removeById(id);
        return Result.success();
    }
}
