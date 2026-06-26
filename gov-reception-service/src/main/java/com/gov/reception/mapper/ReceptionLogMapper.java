package com.gov.reception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.reception.entity.ReceptionLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 办件日志Mapper
 */
@Mapper
public interface ReceptionLogMapper extends BaseMapper<ReceptionLogEntity> {
}
