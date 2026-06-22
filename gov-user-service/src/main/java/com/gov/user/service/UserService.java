package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.user.entity.UserEntity;

/**
 * 用户服务接口
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 根据用户名查询用户
     */
    UserEntity getByUsername(String username);

    /**
     * 根据手机号查询用户
     */
    UserEntity getByPhone(String phone);
}