package com.gov.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.user.entity.RoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色菜单关联 Mapper
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenuEntity> {
}
