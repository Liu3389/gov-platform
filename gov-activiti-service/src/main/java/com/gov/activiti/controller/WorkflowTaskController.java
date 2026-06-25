package com.gov.activiti.controller;

import com.gov.activiti.dto.*;
import com.gov.activiti.service.WorkflowTaskService;
import com.gov.activiti.vo.TaskVO;
import com.gov.activiti.vo.TodoTaskVO;
import com.gov.common.annotation.Log;
import com.gov.common.annotation.RequirePermission;
import com.gov.common.result.PageResult;
import com.gov.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 工作流任务 Controller
 * 权限说明：
 * workflow:query — 查询权限（普通用户）
 * workflow:start — 启动流程（窗口受理人员）
 * workflow:approve — 审批权限（部门审批人、部门领导）
 * workflow:claim — 认领任务（部门工作人员）
 * workflow:delegate— 委托任务（部门审批人）
 * workflow:remind — 催办权限（管理人员）
 * workflow:delete — 删除任务（管理人员）
 */
@Tag(name = "工作流任务管理", description = "工作流任务审批、待办、委托、催办等接口")
@RestController
@RequestMapping("/workflow")
@RequiredArgsConstructor
@Validated
public class WorkflowTaskController {

    private final WorkflowTaskService workflowTaskService;

    // ==================== 查询接口（所有登录用户可用） ====================

    @Operation(summary = "查询我的待办任务")
    @GetMapping("/todo")
    public Result<PageResult<TodoTaskVO>> listTodo(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "用户ID（从Token自动获取）") @RequestParam @NotBlank(message = "用户ID不能为空") String userId) {
        return Result.success(workflowTaskService.pageQueryTodo(pageNum, pageSize, userId));
    }

    @Operation(summary = "分页查询所有任务")
    @RequirePermission(value = "workflow:query")
    @GetMapping("/list")
    public Result<PageResult<TaskVO>> list(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") @Min(1) Long pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Max(value = 100, message = "每页最大100条") Long pageSize,
            @Parameter(description = "任务名称（模糊搜索）") @RequestParam(required = false) String taskName,
            @Parameter(description = "流程定义Key") @RequestParam(required = false) String processKey,
            @Parameter(description = "任务状态") @RequestParam(required = false) String status,
            @Parameter(description = "责任人") @RequestParam(required = false) String assignee) {
        return Result.success(workflowTaskService.pageQuery(pageNum, pageSize, taskName, processKey, status, assignee));
    }

    @Operation(summary = "根据ID查询任务详情")
    @RequirePermission(value = "workflow:query")
    @GetMapping("/{id}")
    public Result<TaskVO> getById(@Parameter(description = "任务ID", required = true) @PathVariable Long id) {
        return Result.success(workflowTaskService.getTaskVOById(id));
    }

    // ==================== 流程操作（按角色分权限） ====================

    @Operation(summary = "启动流程（窗口受理后调用）")
    @Log(module = "工作流管理", action = "启动流程")
    @RequirePermission(value = "workflow:start")
    @PostMapping("/process")
    public Result<String> startProcess(@Valid @RequestBody ProcessStartDTO dto) {
        String instanceId = workflowTaskService.startProcess(dto);
        return Result.success("流程启动成功", instanceId);
    }

    @Operation(summary = "审批任务（通过/驳回/转办）")
    @Log(module = "工作流管理", action = "审批任务")
    @RequirePermission(value = "workflow:approve")
    @PostMapping("/approval")
    public Result<Void> completeTask(@Valid @RequestBody TaskCompleteDTO dto) {
        workflowTaskService.completeTask(dto);
        return Result.success();
    }

    @Operation(summary = "认领任务（将任务指派给自己）")
    @Log(module = "工作流管理", action = "认领任务")
    @RequirePermission(value = "workflow:claim")
    @PostMapping("/task/{taskId}/claim")
    public Result<Void> claimTask(
            @Parameter(description = "任务ID", required = true) @PathVariable @NotBlank String taskId,
            @Parameter(description = "用户ID（从Token自动获取）", required = true) @RequestParam @NotBlank String userId) {
        workflowTaskService.claimTask(taskId, userId);
        return Result.success();
    }

    @Operation(summary = "委托任务（将任务转给他人）")
    @Log(module = "工作流管理", action = "委托任务")
    @RequirePermission(value = "workflow:delegate")
    @PostMapping("/task/{taskId}/delegation")
    public Result<Void> delegateTask(
            @Parameter(description = "任务ID", required = true) @PathVariable @NotBlank String taskId,
            @Valid @RequestBody TaskDelegateDTO dto) {
        workflowTaskService.delegateTask(taskId, dto.getToUserId(), dto.getDelegateReason());
        return Result.success();
    }

    @Operation(summary = "催办任务")
    @Log(module = "工作流管理", action = "催办任务")
    @RequirePermission(value = "workflow:remind")
    @PostMapping("/reminder")
    public Result<Void> remindTask(
            @Parameter(description = "催办人ID（从Token自动获取）", required = true) @RequestParam @NotNull(message = "催办人ID不能为空") Long reminderBy,
            @Valid @RequestBody TaskRemindDTO dto) {
        workflowTaskService.remindTask(dto, reminderBy);
        return Result.success();
    }

    // ==================== 管理操作 ====================

    @Operation(summary = "删除任务（逻辑删除）")
    @Log(module = "工作流管理", action = "删除任务")
    @RequirePermission(value = "workflow:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@Parameter(description = "任务ID", required = true) @PathVariable Long id) {
        workflowTaskService.deleteTask(id);
        return Result.success();
    }
}
