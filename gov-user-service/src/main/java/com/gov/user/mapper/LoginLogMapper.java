package com.gov.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.user.entity.LoginLogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登录日志 Mapper
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogEntity> {
}
