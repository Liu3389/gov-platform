package com.gov.datashare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.datashare.entity.DataSourceEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DataSourceMapper extends BaseMapper<DataSourceEntity> {
}
