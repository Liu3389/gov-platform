package com.gov.message.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.message.entity.InboxEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InboxMapper extends BaseMapper<InboxEntity> {
}
