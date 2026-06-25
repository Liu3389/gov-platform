package com.gov.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.user.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单 Mapper
 */
@Mapper
public interface MenuMapper extends BaseMapper<MenuEntity> {
}
