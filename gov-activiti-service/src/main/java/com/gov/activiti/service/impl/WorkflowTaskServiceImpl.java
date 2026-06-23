package com.gov.activiti.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.activiti.entity.WorkflowTaskEntity;
import com.gov.activiti.mapper.WorkflowTaskMapper;
import com.gov.activiti.service.WorkflowTaskService;
import org.springframework.stereotype.Service;

@Service
public class WorkflowTaskServiceImpl extends ServiceImpl<WorkflowTaskMapper, WorkflowTaskEntity> implements WorkflowTaskService {
}
