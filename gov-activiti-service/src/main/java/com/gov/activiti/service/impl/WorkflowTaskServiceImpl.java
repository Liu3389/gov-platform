package com.gov.activiti.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.activiti.dto.ProcessStartDTO;
import com.gov.activiti.dto.TaskCompleteDTO;
import com.gov.activiti.dto.TaskDelegateDTO;
import com.gov.activiti.dto.TaskRemindDTO;
import com.gov.activiti.entity.*;
import com.gov.activiti.mapper.*;
import com.gov.activiti.service.WorkflowTaskService;
import com.gov.activiti.vo.OpinionVO;
import com.gov.activiti.vo.TaskVO;
import com.gov.activiti.vo.TodoTaskVO;
import com.gov.common.constant.WorkflowConstants;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowTaskServiceImpl extends ServiceImpl<WorkflowTaskMapper, WorkflowTaskEntity>
    implements WorkflowTaskService {

  private final RuntimeService runtimeService;
  private final TaskService taskService;
  private final WorkflowInstanceMapper workflowInstanceMapper;
  private final WorkflowProcessMapper workflowProcessMapper;
  private final WorkflowOpinionMapper workflowOpinionMapper;
  private final WorkflowReminderMapper workflowReminderMapper;
  private final WorkflowDelegateMapper workflowDelegateMapper;

  @Override
  public PageResult<TaskVO> pageQuery(Long pageNum, Long pageSize, String taskName, String processKey, String status,
      String assignee) {
    LambdaQueryWrapper<WorkflowTaskEntity> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(WorkflowTaskEntity::getDeleted, 0);
    wrapper.like(StrUtil.isNotBlank(taskName), WorkflowTaskEntity::getTaskName, taskName);
    wrapper.eq(StrUtil.isNotBlank(processKey), WorkflowTaskEntity::getProcessKey, processKey);
    wrapper.eq(StrUtil.isNotBlank(status), WorkflowTaskEntity::getStatus, status);
    wrapper.eq(StrUtil.isNotBlank(assignee), WorkflowTaskEntity::getAssignee, assignee);
    wrapper.orderByDesc(WorkflowTaskEntity::getCreateTime);

    Page<WorkflowTaskEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
    List<TaskVO> voList = page.getRecords().stream()
        .map(this::toTaskVO)
        .collect(Collectors.toList());
    return new PageResult<>(voList, page.getTotal(), page.getCurrent(), page.getSize());
  }

  @Override
  public TaskVO getTaskVOById(Long id) {
    WorkflowTaskEntity entity = this.getById(id);
    if (entity == null || entity.getDeleted() == 1) {
      throw BusinessException.notFound("任务不存在");
    }
    return toTaskVO(entity);
  }

  @Override
  public PageResult<TodoTaskVO> pageQueryTodo(Long pageNum, Long pageSize, String userId) {
    LambdaQueryWrapper<WorkflowTaskEntity> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(WorkflowTaskEntity::getDeleted, 0);
    wrapper.eq(WorkflowTaskEntity::getAssignee, userId);
    wrapper.in(WorkflowTaskEntity::getStatus, "0", "1");
    wrapper.orderByDesc(WorkflowTaskEntity::getCreateTime);

    Page<WorkflowTaskEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
    List<TodoTaskVO> voList = page.getRecords().stream()
        .map(entity -> {
          TodoTaskVO vo = new TodoTaskVO();
          vo.setTaskId(entity.getTaskId());
          vo.setTaskName(entity.getTaskName());
          vo.setInstanceId(entity.getInstanceId());
          vo.setApplyNo(getApplyNoByInstance(entity.getInstanceId()));
          vo.setItemName(getItemNameByInstance(entity.getInstanceId()));
          vo.setUserName(getUserNameByInstance(entity.getInstanceId()));
          vo.setStartTime(entity.getStartTime());
          vo.setDueTime(entity.getDueTime());
          vo.setPriority(entity.getPriority());
          return vo;
        })
        .collect(Collectors.toList());
    return new PageResult<>(voList, page.getTotal(), page.getCurrent(), page.getSize());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String startProcess(ProcessStartDTO dto) {
    // 构建流程变量
    Map<String, Object> variables = new HashMap<>();
    variables.put(WorkflowConstants.VAR_APPLY_NO, dto.getApplyNo());
    variables.put(WorkflowConstants.VAR_USER_ID, dto.getUserId());
    variables.put(WorkflowConstants.VAR_ITEM_ID, dto.getItemId());
    variables.put(WorkflowConstants.VAR_DEPT_ID, dto.getDeptId());
    if (dto.getVariables() != null) {
      variables.putAll(dto.getVariables());
    }

    // 启动 Activiti 流程实例
    ProcessInstance pi = runtimeService.startProcessInstanceByKey(dto.getProcessKey(), variables);
    String instanceId = pi.getProcessInstanceId();
    log.info("流程启动成功：processKey={}, instanceId={}, applyNo={}", dto.getProcessKey(), instanceId, dto.getApplyNo());

    // 保存流程实例扩展信息
    WorkflowInstanceEntity instanceEntity = new WorkflowInstanceEntity();
    instanceEntity.setInstanceId(instanceId);
    instanceEntity.setProcessKey(dto.getProcessKey());
    instanceEntity.setApplyNo(dto.getApplyNo());
    instanceEntity.setItemId(dto.getItemId());
    instanceEntity.setItemName(dto.getItemName());
    instanceEntity.setUserId(dto.getUserId());
    instanceEntity.setUserName(dto.getUserName());
    instanceEntity.setDeptId(dto.getDeptId());
    instanceEntity.setStartTime(LocalDateTime.now());
    instanceEntity.setStatus("0");
    workflowInstanceMapper.insert(instanceEntity);

    // 同步保存当前任务信息
    syncCurrentTask(instanceId, dto.getProcessKey());

    return instanceId;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void completeTask(TaskCompleteDTO dto) {
    Task task = taskService.createTaskQuery().taskId(dto.getTaskId()).singleResult();
    if (task == null) {
      throw BusinessException.notFound("任务不存在或已完成");
    }

    // 设置审批结果变量
    Map<String, Object> variables = new HashMap<>();
    variables.put(WorkflowConstants.VAR_APPROVAL_RESULT, Integer.parseInt(dto.getApprovalResult()));
    variables.put(WorkflowConstants.VAR_APPROVAL_OPINION, dto.getOpinion());

    if ("3".equals(dto.getApprovalResult()) && StrUtil.isNotBlank(dto.getTargetAssignee())) {
      // 转办：设置下一审批人
      variables.put(WorkflowConstants.VAR_NEXT_ASSIGNEE, dto.getTargetAssignee());
    }

    // 完成任务
    taskService.complete(dto.getTaskId(), variables);
    log.info("任务完成：taskId={}, result={}", dto.getTaskId(), dto.getApprovalResult());

    // 保存审批意见
    saveOpinion(task, dto);

    // 更新任务扩展表状态
    updateTaskStatus(dto.getTaskId(), "2", LocalDateTime.now());

    // 同步下一个任务
    String instanceId = task.getProcessInstanceId();
    syncCurrentTask(instanceId, task.getProcessDefinitionId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void claimTask(String taskId, String userId) {
    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    if (task == null) {
      throw BusinessException.notFound("任务不存在");
    }
    if (StrUtil.isNotBlank(task.getAssignee())) {
      throw new BusinessException(400, "任务已被认领");
    }
    taskService.claim(taskId, userId);
    log.info("任务认领：taskId={}, userId={}", taskId, userId);

    // 更新扩展表
    WorkflowTaskEntity extTask = getByTaskId(taskId);
    if (extTask != null) {
      extTask.setAssignee(userId);
      extTask.setClaimTime(LocalDateTime.now());
      extTask.setStatus("1");
      this.updateById(extTask);
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void delegateTask(String taskId, Long toUserId, String delegateReason) {
    Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
    if (task == null) {
      throw BusinessException.notFound("任务不存在");
    }
    // Activiti 委托
    taskService.delegateTask(taskId, String.valueOf(toUserId));
    log.info("任务委托：taskId={}, from={}, to={}", taskId, task.getAssignee(), toUserId);

    // 保存委托记录
    WorkflowDelegateEntity delegate = new WorkflowDelegateEntity();
    delegate.setTaskId(taskId);
    delegate.setInstanceId(task.getProcessInstanceId());
    delegate.setFromUserId(StrUtil.isNotBlank(task.getAssignee()) ? Long.valueOf(task.getAssignee()) : null);
    delegate.setToUserId(toUserId);
    delegate.setDelegateType("1");
    delegate.setDelegateTime(LocalDateTime.now());
    delegate.setDelegateReason(delegateReason);
    delegate.setStatus("0");
    workflowDelegateMapper.insert(delegate);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void remindTask(TaskRemindDTO dto, Long reminderBy) {
    WorkflowInstanceEntity instance = getByInstanceId(dto.getInstanceId());
    if (instance == null) {
      throw BusinessException.notFound("流程实例不存在");
    }

    WorkflowReminderEntity reminder = new WorkflowReminderEntity();
    reminder.setInstanceId(dto.getInstanceId());
    reminder.setTaskId(dto.getTaskId());
    reminder.setApplyNo(instance.getApplyNo());
    reminder.setReminderType("1");
    reminder.setReminderLevel("NORMAL");
    reminder.setReminderTime(LocalDateTime.now());
    reminder.setReminderBy(reminderBy);
    reminder.setReminderContent(dto.getReminderContent());
    reminder.setStatus("0");
    workflowReminderMapper.insert(reminder);
    log.info("催办记录已保存：instanceId={}", dto.getInstanceId());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteTask(Long id) {
    WorkflowTaskEntity entity = this.getById(id);
    if (entity == null || entity.getDeleted() == 1) {
      throw BusinessException.notFound("任务不存在");
    }
    this.removeById(id);
  }

  // ==================== 私有辅助方法 ====================

  /** 同步当前Activiti任务到扩展表 */
  private void syncCurrentTask(String instanceId, String processKeyOrDefId) {
    String processKey = processKeyOrDefId.contains(":") ? processKeyOrDefId.split(":")[0] : processKeyOrDefId;
    List<Task> tasks = taskService.createTaskQuery().processInstanceId(instanceId).list();
    for (Task task : tasks) {
      WorkflowTaskEntity entity = getByTaskId(task.getId());
      if (entity == null) {
        entity = new WorkflowTaskEntity();
      }
      entity.setTaskId(task.getId());
      entity.setInstanceId(instanceId);
      entity.setProcessKey(processKey);
      entity.setTaskName(task.getName());
      entity.setTaskKey(task.getTaskDefinitionKey());
      entity.setAssignee(task.getAssignee());
      entity.setStartTime(LocalDateTime.now());
      entity.setDueTime(task.getDueDate() != null
          ? LocalDateTime.ofInstant(task.getDueDate().toInstant(), java.time.ZoneId.systemDefault())
          : null);
      entity.setStatus("0");
      entity.setPriority(task.getPriority());
      this.saveOrUpdate(entity);
    }
  }

  /** 更新任务状态 */
  private void updateTaskStatus(String taskId, String status, LocalDateTime endTime) {
    WorkflowTaskEntity entity = getByTaskId(taskId);
    if (entity != null) {
      entity.setStatus(status);
      entity.setEndTime(endTime);
      if (entity.getStartTime() != null) {
        entity.setDuration(java.time.Duration.between(entity.getStartTime(), endTime).toMillis());
      }
      this.updateById(entity);
    }
  }

  /** 保存审批意见 */
  private void saveOpinion(Task task, TaskCompleteDTO dto) {
    WorkflowInstanceEntity instance = getByInstanceId(task.getProcessInstanceId());
    WorkflowOpinionEntity opinion = new WorkflowOpinionEntity();
    opinion.setTaskId(dto.getTaskId());
    opinion.setInstanceId(task.getProcessInstanceId());
    opinion.setApplyNo(instance != null ? instance.getApplyNo() : "");
    opinion.setOperatorId(StrUtil.isNotBlank(task.getAssignee()) ? Long.valueOf(task.getAssignee()) : null);
    opinion.setOpinionType(dto.getApprovalResult());
    opinion.setOpinionContent(dto.getOpinion());
    opinion.setOperateTime(LocalDateTime.now());
    opinion.setNextAssignee(dto.getTargetAssignee());
    workflowOpinionMapper.insert(opinion);
  }

  /** 根据 taskId 查询扩展表 */
  private WorkflowTaskEntity getByTaskId(String taskId) {
    return this.lambdaQuery()
        .eq(WorkflowTaskEntity::getTaskId, taskId)
        .eq(WorkflowTaskEntity::getDeleted, 0)
        .one();
  }

  /** 根据 instanceId 查询流程实例扩展 */
  private WorkflowInstanceEntity getByInstanceId(String instanceId) {
    return workflowInstanceMapper.selectOne(
        new LambdaQueryWrapper<WorkflowInstanceEntity>()
            .eq(WorkflowInstanceEntity::getInstanceId, instanceId)
            .eq(WorkflowInstanceEntity::getDeleted, 0));
  }

  private String getApplyNoByInstance(String instanceId) {
    WorkflowInstanceEntity entity = getByInstanceId(instanceId);
    return entity != null ? entity.getApplyNo() : null;
  }

  private String getItemNameByInstance(String instanceId) {
    WorkflowInstanceEntity entity = getByInstanceId(instanceId);
    return entity != null ? entity.getItemName() : null;
  }

  private String getUserNameByInstance(String instanceId) {
    WorkflowInstanceEntity entity = getByInstanceId(instanceId);
    return entity != null ? entity.getUserName() : null;
  }

  private TaskVO toTaskVO(WorkflowTaskEntity entity) {
    TaskVO vo = new TaskVO();
    BeanUtil.copyProperties(entity, vo);
    return vo;
  }
}
