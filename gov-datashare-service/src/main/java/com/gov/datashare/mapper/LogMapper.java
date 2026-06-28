package com.gov.datashare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.datashare.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<LogEntity> {
}
