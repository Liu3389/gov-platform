package com.gov.item.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.item.entity.ItemEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper extends BaseMapper<ItemEntity> {
}
