package com.gov.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.activiti.entity.WorkflowHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkflowHistoryMapper extends BaseMapper<WorkflowHistoryEntity> {
}
