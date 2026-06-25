package com.gov.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gov.common.result.PageResult;
import com.gov.common.vo.UserVO;
import com.gov.user.dto.LoginDTO;
import com.gov.user.dto.RegisterDTO;
import com.gov.user.dto.UserDTO;
import com.gov.user.entity.UserEntity;
import com.gov.user.vo.LoginVO;

public interface UserService extends IService<UserEntity> {

    UserEntity getByUsername(String username);

    UserEntity getByPhone(String phone);

    LoginVO login(LoginDTO dto);

    void register(RegisterDTO dto);

    PageResult<UserVO> pageQuery(Long pageNum, Long pageSize, String username);

    UserVO getVOById(Long id);

    UserVO getVOByUsername(String username);

    void addUser(UserDTO dto);

    void updateUser(UserDTO dto);

    void deleteUser(Long id);
}
