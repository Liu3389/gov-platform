package com.gov.reception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.reception.entity.QueueEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 排队叫号Mapper
 */
@Mapper
public interface QueueMapper extends BaseMapper<QueueEntity> {
}
