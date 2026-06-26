package com.gov.reception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.reception.entity.OutputEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出件记录Mapper
 */
@Mapper
public interface OutputMapper extends BaseMapper<OutputEntity> {
}
