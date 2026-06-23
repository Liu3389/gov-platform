package com.gov.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.activiti.entity.WorkflowTaskEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkflowTaskMapper extends BaseMapper<WorkflowTaskEntity> {
}
