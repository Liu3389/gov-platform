package com.gov.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.user.entity.UserEntity;
import com.gov.user.mapper.UserMapper;
import com.gov.user.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public UserEntity getByUsername(String username) {
        return this.lambdaQuery()
                .eq(UserEntity::getUsername, username)
                .eq(UserEntity::getDeleted, 0)
                .one();
    }

    @Override
    public UserEntity getByPhone(String phone) {
        return this.lambdaQuery()
                .eq(UserEntity::getPhone, phone)
                .eq(UserEntity::getDeleted, 0)
                .one();
    }
}