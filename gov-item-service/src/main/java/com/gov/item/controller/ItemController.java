package com.gov.item.controller;

import com.gov.common.annotation.Log;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import com.gov.item.dto.ItemDTO;
import com.gov.item.service.ItemService;
import com.gov.item.vo.ItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;

@Tag(name = "事项管理", description = "事项信息管理接口")
@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @Operation(summary = "分页查询事项（支持按名称、类型筛选）")
    @GetMapping("/list")
    public Result<PageResult<ItemVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "事项名称（模糊搜索）") @RequestParam(required = false) String itemName,
            @Parameter(description = "事项类型：1行政许可 2公共服务 3行政确认") @RequestParam(required = false) Integer itemType) {
        return Result.success(itemService.pageQueryVO(pageNum, pageSize, itemName, itemType));
    }

    @Operation(summary = "根据ID查询")
    @GetMapping("/{id}")
    public Result<ItemVO> getById(@Parameter(description = "事项ID") @PathVariable Long id) {
        return Result.success(itemService.getVOById(id));
    }

    @Operation(summary = "根据事项编码查询")
    @GetMapping("/byCode")
    public Result<ItemVO> getByCode(@Parameter(description = "事项编码") @RequestParam String itemCode) {
        return Result.success(itemService.getVOByItemCode(itemCode));
    }

    @Operation(summary = "新增事项")
    @Log(module = "事项管理", action = "新增事项")
    @PostMapping
    public Result<Void> add(@Parameter(description = "事项信息") @Valid @RequestBody ItemDTO dto) {
        itemService.addItem(dto);
        return Result.success();
    }

    @Operation(summary = "更新事项")
    @Log(module = "事项管理", action = "更新事项")
    @PutMapping
    public Result<Void> update(@Parameter(description = "事项信息") @Valid @RequestBody ItemDTO dto) {
        itemService.updateItem(dto);
        return Result.success();
    }

    @Operation(summary = "删除事项（逻辑删除）")
    @Log(module = "事项管理", action = "删除事项")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "事项ID") @PathVariable Long id) {
        itemService.deleteItem(id);
        return Result.success();
    }
}
