package com.gov.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.monitor.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<LogEntity> {
}
