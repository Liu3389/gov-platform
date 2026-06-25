package com.gov.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.activiti.entity.WorkflowInstanceEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkflowInstanceMapper extends BaseMapper<WorkflowInstanceEntity> {
}
