package com.gov.activiti.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.activiti.dto.TaskCompleteDTO;
import com.gov.activiti.dto.TaskRemindDTO;
import com.gov.activiti.dto.ProcessStartDTO;
import com.gov.activiti.entity.WorkflowTaskEntity;
import com.gov.activiti.vo.TaskVO;
import com.gov.activiti.vo.TodoTaskVO;
import com.gov.common.result.PageResult;

/**
 * 工作流任务服务接口
 */
public interface WorkflowTaskService extends IService<WorkflowTaskEntity> {

  /** 分页查询任务 */
  PageResult<TaskVO> pageQuery(Long pageNum, Long pageSize, String taskName, String processKey, String status,
      String assignee);

  /** 根据ID查询任务详情 */
  TaskVO getTaskVOById(Long id);

  /** 查询待办任务列表 */
  PageResult<TodoTaskVO> pageQueryTodo(Long pageNum, Long pageSize, String userId);

  /** 启动流程 */
  String startProcess(ProcessStartDTO dto);

  /** 完成任务（审批通过/驳回/转办） */
  void completeTask(TaskCompleteDTO dto);

  /** 认领任务 */
  void claimTask(String taskId, String userId);

  /** 委托任务 */
  void delegateTask(String taskId, Long toUserId, String delegateReason);

  /** 催办 */
  void remindTask(TaskRemindDTO dto, Long reminderBy);

  /** 删除任务（逻辑删除） */
  void deleteTask(Long id);
}
