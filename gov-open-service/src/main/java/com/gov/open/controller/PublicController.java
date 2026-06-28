package com.gov.open.controller;

import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.open.dto.NoticeQueryDTO;
import com.gov.open.service.NoticeService;
import com.gov.open.service.PolicyService;
import com.gov.open.vo.NoticeVO;
import com.gov.open.vo.PolicyVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 面向公众的只读接口（无需登录）
 */
@Tag(name = "政务公开", description = "面向公众的只读接口")
@RestController
@RequestMapping("/open/public")
@RequiredArgsConstructor
@Validated
public class PublicController {

    private final NoticeService noticeService;
    private final PolicyService policyService;

    @Operation(summary = "公告列表（已发布）")
    @GetMapping("/notice/list")
    public Result<PageResult<NoticeVO>> noticeList(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "关键词") @RequestParam(required = false) String keyword) {
        NoticeQueryDTO queryDTO = new NoticeQueryDTO();
        queryDTO.setKeyword(keyword);
        queryDTO.setStatus(1); // 只查已发布
        return Result.success(noticeService.pageQueryVO(pageNum, pageSize, queryDTO));
    }

    @Operation(summary = "公告详情")
    @GetMapping("/notice/{id}")
    public Result<NoticeVO> noticeDetail(@Parameter(description = "公告ID") @PathVariable Long id) {
        NoticeVO vo = noticeService.getVOById(id);
        if (vo == null || !"1".equals(vo.getStatus())) {
            return Result.notFound("公告不存在");
        }
        // 增加浏览次数
        noticeService.incrementViewCount(id);
        return Result.success(vo);
    }

    @Operation(summary = "政策法规列表（已发布）")
    @GetMapping("/policy/list")
    public Result<PageResult<PolicyVO>> policyList(
            @Parameter(description = "页码") @Min(1) @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "政策类型") @RequestParam(required = false) Integer policyType) {
        return Result.success(policyService.pageQueryVO(pageNum, pageSize, policyType, 1));
    }

    @Operation(summary = "政策法规详情")
    @GetMapping("/policy/{id}")
    public Result<PolicyVO> policyDetail(@Parameter(description = "政策ID") @PathVariable Long id) {
        PolicyVO vo = policyService.getVOById(id);
        if (vo == null || !"1".equals(vo.getStatus())) {
            return Result.notFound("政策法规不存在");
        }
        return Result.success(vo);
    }
}
