package com.gov.open.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.open.entity.ContentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 内容审核Mapper
 */
@Mapper
public interface ContentMapper extends BaseMapper<ContentEntity> {
}
