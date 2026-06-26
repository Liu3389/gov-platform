package com.gov.open.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.open.entity.FeedbackEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 公开反馈Mapper
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<FeedbackEntity> {
}
