package com.gov.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 任务创建监听器
 * 在任务创建时自动记录日志。
 * BPMN 引用方式：<activiti:taskListener event="create" class="com.gov.activiti.listener.TaskCreateListener" />
 */
@Slf4j
@Component
public class TaskCreateListener implements TaskListener {

    @Override
    public void notify(DelegateTask task) {
        String taskId = task.getId();
        String taskName = task.getName();
        String processInstanceId = task.getProcessInstanceId();
        String taskKey = task.getTaskDefinitionKey();

        log.info("【任务创建】taskId={}, taskName={}, taskKey={}, instanceId={}",
                taskId, taskName, taskKey, processInstanceId);
    }
}
