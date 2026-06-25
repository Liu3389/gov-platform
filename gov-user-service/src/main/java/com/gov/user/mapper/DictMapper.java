package com.gov.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.user.entity.DictEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据字典 Mapper
 */
@Mapper
public interface DictMapper extends BaseMapper<DictEntity> {
}
