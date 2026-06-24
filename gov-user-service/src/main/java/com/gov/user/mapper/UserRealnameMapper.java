package com.gov.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gov.user.entity.UserRealnameEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 实名认证记录 Mapper
 */
@Mapper
public interface UserRealnameMapper extends BaseMapper<UserRealnameEntity> {
}
