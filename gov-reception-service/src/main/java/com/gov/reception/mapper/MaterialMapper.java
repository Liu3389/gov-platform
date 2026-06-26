package com.gov.reception.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.reception.entity.MaterialEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 申报材料Mapper
 */
@Mapper
public interface MaterialMapper extends BaseMapper<MaterialEntity> {
}
