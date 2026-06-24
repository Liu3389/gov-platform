package com.gov.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gov.common.exception.BusinessException;
import com.gov.common.result.PageResult;
import com.gov.common.utils.JwtUtil;
import com.gov.common.vo.UserVO;
import com.gov.user.dto.LoginDTO;
import com.gov.user.dto.RegisterDTO;
import com.gov.user.dto.UserDTO;
import com.gov.user.entity.UserEntity;
import com.gov.user.mapper.UserMapper;
import com.gov.user.service.UserService;
import com.gov.user.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 用户服务实现
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Override
    public UserEntity getByUsername(String username) {
        UserEntity user = this.lambdaQuery()
                .eq(UserEntity::getUsername, username)
                .eq(UserEntity::getDeleted, 0)
                .one();
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        return user;
    }

    @Override
    public UserEntity getByPhone(String phone) {
        UserEntity user = this.lambdaQuery()
                .eq(UserEntity::getPhone, phone)
                .eq(UserEntity::getDeleted, 0)
                .one();
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginVO login(LoginDTO dto) {
        UserEntity user = this.lambdaQuery()
                .eq(UserEntity::getUsername, dto.getUsername())
                .eq(UserEntity::getDeleted, 0)
                .one();
        if (user == null) {
            throw BusinessException.notFound("用户名或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new BusinessException(403, "账号已被禁用");
        }
        if (!BCrypt.checkpw(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }
        String token = JwtUtil.generateToken(user.getId(), user.getUsername());
        user.setLastLoginTime(LocalDateTime.now());
        this.updateById(user);
        LoginVO vo = BeanUtil.copyProperties(user, LoginVO.class);
        vo.setToken(token);
        vo.setUserId(user.getId());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO dto) {
        if (this.lambdaQuery()
                .eq(UserEntity::getUsername, dto.getUsername())
                .eq(UserEntity::getDeleted, 0)
                .count() > 0) {
            throw BusinessException.alreadyExists("用户名已存在");
        }
        if (this.lambdaQuery()
                .eq(UserEntity::getPhone, dto.getPhone())
                .eq(UserEntity::getDeleted, 0)
                .count() > 0) {
            throw BusinessException.alreadyExists("手机号已被注册");
        }
        this.save(dto.toEntity());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(UserEntity entity) {
        if (entity.getPassword() != null && !entity.getPassword().startsWith("$2a$")) {
            entity.setPassword(BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt()));
        }
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        return super.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(UserEntity entity) {
        return super.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(java.io.Serializable id) {
        return super.removeById(id);
    }

    // ==================== VO 查询方法 ====================

    @Override
    public PageResult<UserVO> pageQuery(Long pageNum, Long pageSize, String username) {
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getDeleted, 0);
        if (StringUtils.hasText(username)) {
            wrapper.like(UserEntity::getUsername, username);
        }
        wrapper.orderByDesc(UserEntity::getCreateTime);
        wrapper.select(UserEntity.class, f -> !"password".equals(f.getProperty()));
        IPage<UserEntity> page = this.page(new Page<>(pageNum, pageSize), wrapper);
        return PageResult.of(
                page.getRecords().stream().map(this::toVO).collect(java.util.stream.Collectors.toList()),
                page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public UserVO getVOById(Long id) {
        UserEntity user = this.getById(id);
        if (user == null || user.getDeleted() == 1) {
            throw BusinessException.notFound("用户不存在");
        }
        return toVO(user);
    }

    @Override
    public UserVO getVOByUsername(String username) {
        UserEntity user = getByUsername(username);
        return toVO(user);
    }

    private UserVO toVO(UserEntity entity) {
        UserVO vo = BeanUtil.copyProperties(entity, UserVO.class);
        vo.setId(entity.getId());
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserDTO dto) {
        // 检查用户名是否已存在
        long count = this.lambdaQuery()
                .eq(UserEntity::getUsername, dto.getUsername())
                .eq(UserEntity::getDeleted, 0)
                .count();
        if (count > 0) {
            throw BusinessException.alreadyExists("用户名已存在");
        }
        UserEntity entity = dto.toEntity();
        this.save(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserDTO dto) {
        UserEntity exist = this.getById(dto.getId());
        if (exist == null) {
            throw BusinessException.notFound("用户不存在");
        }
        UserEntity entity = dto.toEntity();
        entity.setId(dto.getId());
        this.updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        UserEntity user = this.getById(id);
        if (user == null) {
            throw BusinessException.notFound("用户不存在");
        }
        this.removeById(id);
    }
}
