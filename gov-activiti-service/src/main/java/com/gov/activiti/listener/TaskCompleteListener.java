package com.gov.activiti.listener;

import com.gov.common.constant.WorkflowConstants;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 任务完成监听器
 * 在任务完成时自动记录操作日志。
 * BPMN 引用方式：<activiti:taskListener event="complete" class="com.gov.activiti.listener.TaskCompleteListener" />
 */
@Slf4j
@Component
public class TaskCompleteListener implements TaskListener {

    @Override
    public void notify(DelegateTask task) {
        String taskId = task.getId();
        String taskName = task.getName();
        String processInstanceId = task.getProcessInstanceId();
        String assignee = task.getAssignee();
        Object approvalResult = task.getVariable(WorkflowConstants.VAR_APPROVAL_RESULT);
        Object approvalOpinion = task.getVariable(WorkflowConstants.VAR_APPROVAL_OPINION);

        log.info("【任务完成】taskId={}, taskName={}, instanceId={}, assignee={}, result={}, opinion={}",
                taskId, taskName, processInstanceId, assignee, approvalResult, approvalOpinion);
    }
}
